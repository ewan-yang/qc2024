package com.tecpie.platform.task_user.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.enums.*;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.service.CommonFileService;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskExecuteStatusService;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFeedbackSaveCommand;
import com.tecpie.platform.task_user.controller.assembler.command.save.TaskUserFeedbackLogSaveCommandAssembler;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.entity.TaskUserBackupPhone;
import com.tecpie.platform.task_user.entity.TaskUserFeedbackLog;
import com.tecpie.platform.task_user.mapper.TaskUserFeedbackLogMapper;
import com.tecpie.platform.task_user.service.TaskUserBackupPhoneService;
import com.tecpie.platform.task_user.service.TaskUserFeedbackLogService;
import com.tecpie.platform.task_user.service.TaskUserService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 停电任务用户反馈表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskUserFeedbackLogServiceImpl extends BaseServiceImpl<TaskUserFeedbackLogMapper, TaskUserFeedbackLog> implements TaskUserFeedbackLogService {

    private final TaskUserService taskUserService;
    private final TaskService taskService;
    private final TaskExecuteStatusService taskExecuteStatusService;
    private final TaskUserBackupPhoneService taskUserBackupPhoneService;
    private final CommonFileService commonFileService;

    public TaskUserFeedbackLogServiceImpl(TaskUserService taskUserService, TaskService taskService, TaskUserBackupPhoneService taskUserBackupPhoneService,
                                          TaskExecuteStatusService taskExecuteStatusService, CommonFileService commonFileService) {
        this.taskUserService = taskUserService;
        this.taskService = taskService;
        this.taskExecuteStatusService = taskExecuteStatusService;
        this.taskUserBackupPhoneService = taskUserBackupPhoneService;
        this.commonFileService = commonFileService;
    }

    @Override
    public TaskUserFeedbackLog selectExtensionById(Serializable id) {
        TaskUserFeedbackLog entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务用户反馈表[%s]不存在", id));
        }
        entity.setCommonFileList(commonFileService.searchExtensionList(BusinessTypeEnum.TASK_USER_MODEL.getCode(), id));
        return entity;
    }

    @Override
    public List<TaskUserFeedbackLog> searchExtensionPageList(TaskUserFeedbackLogQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<TaskUserFeedbackLog> searchListByTaskUserId(Serializable taskUserId) {
        TaskUserFeedbackLogQueryCommand command = new TaskUserFeedbackLogQueryCommand();
        command.setTaskUserId(taskUserId);
        List<TaskUserFeedbackLog> taskUserFeedbackList = this.searchExtensionPageList(command);
        for (TaskUserFeedbackLog taskUserFeedbackLog : taskUserFeedbackList) {
            taskUserFeedbackLog.setCommonFileList(commonFileService.searchExtensionList(BusinessTypeEnum.TASK_USER_MODEL.getCode(), taskUserFeedbackLog.getId()));
        }
        return taskUserFeedbackList;
    }

    @Override
    public List<TaskUserFeedbackLog> selectMaxFeedbackTimeList(List<Serializable> taskUserIdList) {
        return this.baseMapper.selectMaxFeedbackTimeList(taskUserIdList);
    }

    @Override
    public Serializable saveTaskUserFeedbackLog(TaskUserFeedbackLog entity) {
        // 获取停电通知用户
        TaskUser taskUser = taskUserService.selectExtensionById(entity.getTaskUserId());
        String feedbackStatus = entity.getFeedbackStatus();
        // 停电任务
        Task task = taskUser.getTask();
        // 停电任务执行状态
        String executeStatus = TaskUtil.getTaskExecuteStatus(task);
        if (TaskUtil.executeStatusYwc(executeStatus)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务[%s]已完成，不能进行反馈操作", task.getTaskCode()));
        }
        // 取消通知反馈
        if (TaskUtil.executeStatusYqx(executeStatus)) {
            String cancelAssignStatus = taskUser.getCancelAssignStatus();
            // 必须是已派发或已反馈的数据能操作
            if (!TaskUserCancelAssignStatusEnum.YPF.getCode().equals(cancelAssignStatus) && !TaskUserCancelAssignStatusEnum.YFK.getCode().equals(cancelAssignStatus)) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务用户[%s]取消通知派发状态必须已派发或已反馈才能进行反馈操作", taskUser.getReceiptCode()));
            }
            // 验证枚举值是否合法
            if (!TaskUserCancelFeedbackStatusEnum.validValue(feedbackStatus)) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("取消通知反馈状态[%s]不合法，不能进行反馈操作", feedbackStatus));
            }
            taskUser.setCancelFeedbackStatus(feedbackStatus);
            taskUser.setCancelFeedbackTime(LocalDateTime.now());
            taskUser.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.YFK.getCode());
            taskUser.setCancelAssignMethod(entity.getDeliveryMethod());
            // 反馈类型
            entity.setFeedbackType(TaskUserFeedbackTypeEnum.QX.getCode());
        } else {
            String assignStatus = taskUser.getAssignStatus();
            // 必须是已派发或已反馈的数据能操作
            if (!TaskUserAssignStatusEnum.YPF.getCode().equals(assignStatus) && !TaskUserAssignStatusEnum.YFK.getCode().equals(assignStatus)) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务用户[%s]派发状态不是已派发或已反馈，不能进行反馈操作", taskUser.getReceiptCode()));
            }
            // 验证枚举值是否合法
            if (!TaskUserFeedbackStatusEnum.validValue(feedbackStatus)) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("反馈状态[%s]不合法，不能进行反馈操作", feedbackStatus));
            }
            taskUser.setFeedbackStatus(feedbackStatus);
            taskUser.setFeedbackTime(LocalDateTime.now());
            taskUser.setAssignStatus(TaskUserAssignStatusEnum.YFK.getCode());
            taskUser.setAssignMethod(entity.getDeliveryMethod());
            // 反馈类型
            entity.setFeedbackType(TaskUserFeedbackTypeEnum.ZC.getCode());
            // 如果是拒签
            if (TaskUserFeedbackStatusEnum.JQ.getCode().equals(feedbackStatus)) {
                taskUser.setRejectedReason(entity.getRejectedReason());
                // 用户拒签告警
                ThreadUtil.execAsync(() -> {
                    taskUserService.userRejectAdvance(taskUser);
                });
            }
        }
        // 备用联系方式
        String backupPhone = entity.getBackupPhone();
        if (StringUtils.isNotBlank(backupPhone)) {
            taskUser.setBackupPhone(backupPhone);
            // 保存或更新备用联系方式
            TaskUserBackupPhone taskUserBackupPhone = new TaskUserBackupPhone();
            taskUserBackupPhone.setAccountNumber(taskUser.getAccountNumber());
            taskUserBackupPhone.setBackupPhone(backupPhone);
            taskUserBackupPhoneService.saveTaskUserBackupPhone(taskUserBackupPhone);
        }
        // 保存反馈记录
        entity.setId(IdWorker.getId());
        this.save(entity);
        // 更新用户
        taskUserService.updateById(taskUser);
        // 停电任务通知用户
        List<TaskUser> taskUserList = taskUserService.searchListByTaskId(task.getId());
        // 是否全部反馈
        boolean boolAllFeedback = true;
        for (TaskUser item : taskUserList) {
            // 已取消
            if (TaskUtil.executeStatusYqx(executeStatus)) {
                // 忽略不派发的用户
                if (TaskUserCancelAssignStatusEnum.BPF.getCode().equals(item.getCancelAssignStatus())) {
                    continue;
                }
                // 只要有一条是未签，则不更新
                if (TaskUserCancelFeedbackStatusEnum.WQ.getCode().equals(item.getCancelFeedbackStatus())) {
                    boolAllFeedback = false;
                    break;
                }
                continue;
            }
            // 忽略不派发的用户
            if (TaskUserAssignStatusEnum.BPF.getCode().equals(item.getAssignStatus())) {
                continue;
            }
            // 只要有一条是未签，则不更新
            if (TaskUserFeedbackStatusEnum.WQ.getCode().equals(item.getFeedbackStatus())) {
                boolAllFeedback = false;
                break;
            }
        }
        // 更新任务状态
        if (boolAllFeedback) {
            if (!TaskUtil.executeStatusYqx(executeStatus) ) {
                String newExecuteStatus = TaskExecuteStatusEnum.YFK.getCode();
                task.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(task.getId(), newExecuteStatus));
            }
        }
        task.setTaskUserList(taskUserList);
        TaskUtil.calQty(task);
        taskService.updateById(task);
        // 更新图片id
        List<CommonFile> commonFileList = entity.getCommonFileList();
        if (CollectionUtils.isNotEmpty(commonFileList)) {
            commonFileService.updateBusinessId(commonFileList, BusinessTypeEnum.TASK_USER_MODEL.getCode(), entity.getId());
        }
        return entity.getId();
    }

    @Override
    public List<CommonFile> batchUpload(MultipartFile[] fileList) {
        boolean b = this.areAllFilesPngOrJpg(fileList);
        if (!b) {
            throw BusinessException.build("仅限上传JPG和PNG文件");
        }
        return commonFileService.batchUpload(fileList);
    }

    @Override
    public void oneTouchFeedback(TaskUserFeedbackSaveCommand command) {
        LambdaQueryChainWrapper<TaskUser> wrapper = taskUserService.lambdaQuery();
        // 先判断第一优先级：被选中的，需要一键反馈的用户
        if (CollectionUtils.isNotEmpty(command.getTaskUserIdList())) {
            wrapper = wrapper
                    .in(TaskUser::getId, command.getTaskUserIdList())
                    // 仅针对 已派发 且 未签 的用户
                    .eq(TaskUser::getAssignStatus, TaskUserAssignStatusEnum.YPF.getCode())
                    .eq(TaskUser::getFeedbackStatus, TaskUserFeedbackStatusEnum.WQ.getCode())
                    .eq(TaskUser::getDeleted, 0).eq(TaskUser::getStatus, 1);

        } else {
            // 如果第一优先级为空，开始进行第二优先级查找
            Serializable taskId = command.getTaskId();
            // 判断当前通知单是否是执行中，如果是非执行中，不允许一键派发
            Task task = taskService.selectExtensionById(taskId);
            if (!task.getTaskExecuteStatus().getExecuteStatus().equals(TaskExecuteStatusEnum.ZXZ.getCode())) {
                throw BusinessException.build("当前通知单未处于执行中，无法一键反馈！");
            }
            wrapper = wrapper
                    // 通知单id
                    .eq(TaskUser::getTaskId, taskId)
                    // 用户类型，台区，名称，地址
                    .in(CollectionUtils.isNotEmpty(command.getUserTypeList()), TaskUser::getUserType, command.getUserTypeList())
                    .like(StringUtils.isNotBlank(command.getRegion()), TaskUser::getRegion, command.getRegion())
                    .like(StringUtils.isNotBlank(command.getCustomerName()), TaskUser::getCustomerName, command.getCustomerName())
                    .like(StringUtils.isNotBlank(command.getCustomerAddress()), TaskUser::getCustomerName, command.getCustomerAddress())
                    // 仅针对 已派发 且 未签 的用户
                    .eq(TaskUser::getAssignStatus, TaskUserAssignStatusEnum.YPF.getCode())
                    .eq(TaskUser::getFeedbackStatus, TaskUserFeedbackStatusEnum.WQ.getCode())
                    // 未删除，已启用
                    .eq(TaskUser::getDeleted, 0).eq(TaskUser::getStatus, 1);
        }
        List<TaskUser> taskUserList = wrapper.list();
        if (CollectionUtils.isEmpty(taskUserList)) {
            throw BusinessException.build("当前通知单未存在任何可以一键签收的用户！");
        }
        List<CompletableFuture<Void>> futureList = Lists.newArrayList();
        // 多线程模式处理，将taskUser以30个为一组划分
        List<List<TaskUser>> partitionList = partitionList(taskUserList);
        for (List<TaskUser> list : partitionList) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                TaskUserFeedbackLog parse = TaskUserFeedbackLogSaveCommandAssembler.INSTANCE.parse(command);
                for (TaskUser taskUser : list) {
                    parse.setTaskUserId(taskUser.getId());
                    this.saveTaskUserFeedbackLog(parse);
                }
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    private boolean areAllFilesPngOrJpg(MultipartFile[] fileList) {
        if (fileList == null || fileList.length == 0) {
            return false;  // 或者根据需求返回 true，如果你认为空数组应该被视为有效
        }
        for (MultipartFile file : fileList) {
            if (file == null) {
                return false;
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || !(fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg"))) {
                return false;
            }
        }
        return true;
    }

    private static List<List<TaskUser>> partitionList(List<TaskUser> list) {
        List<List<com.tecpie.platform.task_user.entity.TaskUser>> partitions = Lists.newArrayList();
        for (int i = 0; i < list.size(); i += 30) {
            partitions.add(new ArrayList<>(list.subList(i, Math.min(i + 30, list.size()))));
        }
        return partitions;
    }

}