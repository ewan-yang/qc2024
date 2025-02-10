package com.tecpie.platform.task.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.constant.CommonConstants;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.TaskConstants;
import com.tecpie.platform.common.enums.*;
import com.tecpie.platform.common.feign.advance.AdvanceNoticeFeignClient;
import com.tecpie.platform.common.feign.advanceRule.AdvanceRuleFeignClient;
import com.tecpie.platform.common.util.FileUtil;
import com.tecpie.platform.common.util.Str2HexUtil;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.service.CommonFileService;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.save.AdvanceParamCommand;
import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.service.PlanItemService;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import com.tecpie.platform.seq.service.SeqGenService;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskTotalQueryCommand;
import com.tecpie.platform.task.api.command.save.TaskOutSaveCommand;
import com.tecpie.platform.task.api.command.update.TaskCancelCommand;
import com.tecpie.platform.task.api.resource.TaskSumTotalResource;
import com.tecpie.platform.task.api.resource.TaskTotalResource;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import com.tecpie.platform.task.mapper.TaskMapper;
import com.tecpie.platform.task.service.TaskExecuteStatusService;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.platform.task_notice.service.TaskNoticeService;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserOutSaveCommand;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.service.TaskUserService;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 停电任务表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends BaseServiceImpl<TaskMapper, Task> implements TaskService {

    private TaskExecuteStatusService taskExecuteStatusService;
    private TaskUserService taskUserService;
    private SysUserService sysUserService;
    private SeqGenService seqGenService;
    private CommonFileService commonFileService;
    private AdvanceRuleFeignClient advanceRuleFeignClient;
    private AdvanceNoticeFeignClient advanceNoticeFeignClient;
    private TaskNoticeService taskNoticeService;
    private PlanItemService planItemService;
    private SysLovLineService sysLovLineService;

    @Autowired
    @Lazy
    private void setPlantItemService(PlanItemService planItemService) {
        this.planItemService = planItemService;
    }

    @Autowired
    public void setTaskServiceImpl(TaskExecuteStatusService taskExecuteStatusService,
                                   TaskUserService taskUserService,
                                   SysUserService sysUserService,
                                   SeqGenService seqGenService,
                                   CommonFileService commonFileService,
                                   AdvanceRuleFeignClient advanceRuleFeignClient,
                                   AdvanceNoticeFeignClient advanceNoticeFeignClient,
                                   TaskNoticeService taskNoticeService,
                                   SysLovLineService sysLovLineService) {
        this.taskExecuteStatusService = taskExecuteStatusService;
        this.taskUserService = taskUserService;
        this.sysUserService = sysUserService;
        this.seqGenService = seqGenService;
        this.commonFileService = commonFileService;
        this.advanceRuleFeignClient = advanceRuleFeignClient;
        this.advanceNoticeFeignClient = advanceNoticeFeignClient;
        this.taskNoticeService = taskNoticeService;

        this.sysLovLineService = sysLovLineService;
    }

    public static final String SM4_KEY = "B56977BC73144A568B4599453D655BB9";

    @Override
    public Task selectExtensionById(Serializable id) {
        Task entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电通知表[%s]不存在", id));
        }
        if (ObjectUtils.isNotEmpty(entity.getPlanItemId())) {
            PlanItem planItem = planItemService.lambdaQuery().eq(PlanItem::getId, entity.getPlanItemId()).one();
            if (ObjectUtils.isNotEmpty(planItem)) {
                entity.setProjectName(planItem.getProjectName());
            }
        }
        entity.setCommonFileList(commonFileService.searchExtensionList(BusinessTypeEnum.TASK_MODEL.getCode(), id));
        // 根据工程队计算数量
        entity.setTaskUserList(taskUserService.searchListByTaskId(entity.getId()));
        TaskUtil.calQty(entity, true);
        return entity;
    }

    @Override
    public List<Task> searchExtensionPageList(TaskQueryCommand command) {
        command.initDateParam();
        List<Task> taskList = this.baseMapper.searchExtensionPageList(command);
        if (!CollectionUtils.isEmpty(taskList)) {
            // 获取当前登录用户信息
            Map<Serializable, SysUser> userMap = sysUserService.getUserListByIds(Lists.newArrayList(taskList.stream().map(Task::getAssignedBy)
                    .filter(assignedBy -> !ObjectUtils.isEmpty(assignedBy)).collect(Collectors.toSet()))).stream().collect(Collectors.toMap(SysUser::getId, e -> e));
            // 查询停电用户
            List<TaskUser> taskUserList = taskUserService.lambdaQuery().in(TaskUser::getTaskId, taskList.stream().map(BaseEntity::getId).collect(Collectors.toList())).list();
            Map<String, List<TaskUser>> taskUserMap = Maps.newHashMap();
            for (TaskUser taskUser : taskUserList) {
                List<TaskUser> dataList = taskUserMap.get(taskUser.getTaskId().toString());
                if (dataList == null) {
                    dataList = Lists.newArrayList();
                }
                dataList.add(taskUser);
                taskUserMap.put(taskUser.getTaskId().toString(), dataList);
            }
            taskList.forEach(e -> {
                SysUser user = userMap.get(e.getAssignedBy());
                if (!ObjectUtils.isEmpty(user)) {
                    e.setAssignedByName(user.getName());
                }
                // 根据工程队计算数量
                e.setTaskUserList(taskUserMap.get(e.getId().toString()));
                TaskUtil.calQty(e, true);
            });
        }
        return taskList;
    }

    @Override
    public List<Task> searchVersionList(Serializable id) {
        // 根据id先获取当前数据
        Task task = this.selectExtensionById(id);
        // 如果关联任务单号为空，则根据当前任务编号去查关联单号
        List<Task> taskList = Lists.newArrayList();
        if (StringUtils.isBlank(task.getAssTaskCode())) {
            // 查询关联单号等于任务编号的数据
            TaskQueryCommand command = new TaskQueryCommand();
            command.setAssTaskCode(task.getTaskCode());
            List<Task> dataList = this.baseMapper.searchExtensionPageList(command);
            if (!CollectionUtils.isEmpty(dataList)) {
                taskList.addAll(dataList);
            }
            taskList.add(task);
        } else {
            // 查询关联单号, 包含了本数据
            TaskQueryCommand command = new TaskQueryCommand();
            command.setAssTaskCode(task.getAssTaskCode());
            List<Task> dataList = this.baseMapper.searchExtensionPageList(command);
            if (!CollectionUtils.isEmpty(dataList)) {
                taskList.addAll(dataList);
            }
            // 查询任务编号等于关联单号的数据，只会有一条数据
            command = new TaskQueryCommand();
            command.setTaskCode(task.getAssTaskCode());
            dataList = this.baseMapper.searchExtensionPageList(command);
            if (!CollectionUtils.isEmpty(dataList)) {
                taskList.addAll(dataList);
            }
        }
        // 排序
        taskList.sort(Comparator.comparing(Task::getVersion));
        return taskList;
    }

    @Override
    public Serializable saveTask(Task entity) {
        // 任务编号
        if (StringUtils.isBlank(entity.getTaskCode())) {
            entity.setTaskCode(seqGenService.getValue(TaskConstants.TASK_CODE_SEQ));
        }
        // 单位名称
        if (StringUtils.isBlank(entity.getUnitName())) {
            entity.setUnitName(TaskConstants.UNIT_NAME);
        }
        entity.setAssignedBy(TecpieSecurityUtil.getUser().getId());
        // 默认版本号
        if (ObjectUtils.isEmpty(entity.getVersion())) {
            entity.setVersion(TaskConstants.DEFAULT_VERSION);
        }
        // 执行状态
        String executeStatus = TaskExecuteStatusEnum.DTJ.getCode();
        if (entity.getSubmitType().equals(TaskConstants.SUBMIT_YES)) {
            executeStatus = TaskExecuteStatusEnum.DPF.getCode();
            // 确认时间
            entity.setConfirmTime(LocalDateTime.now());
        }
        // 计算数量
        TaskUtil.calQty(entity);
        // 保存停电任务
        this.save(entity);
        // 更新执行状态记录
        this.lambdaUpdate().set(Task::getExecuteStatusId, taskExecuteStatusService.saveTaskExecuteStatus(entity.getId(), executeStatus)).eq(Task::getId, entity.getId()).update();
        // 保存停电通知用户
        taskUserService.saveTaskUser(entity, entity.getTaskUserList());
        // 更新附件id
        List<CommonFile> commonFileList = entity.getCommonFileList();
        if (!CollectionUtils.isEmpty(commonFileList)) {
            commonFileService.updateBusinessId(commonFileList, BusinessTypeEnum.TASK_MODEL.getCode(), entity.getId());
        }
        // 是否保存运方下达风险预警信息
        if (entity.getConfirmTime() != null) {
            // 异步调用告警信息
            ThreadUtil.execAsync(() -> {
                try {
                    // 保存预警，返回的是业务ID对应的告警类型
                    Map<String, Integer> alarmTypeMap = this.carrierReleaseAdvance(entity);
                    // 告警状态
                    Integer advanceStatus = alarmTypeMap.get(entity.getId().toString());
                    if (advanceStatus != null) {
                        // 更新告警状态
                        this.lambdaUpdate().set(Task::getAdvanceStatus, advanceStatus).eq(Task::getId, entity.getId()).update();
                    }
                } catch (Exception e) {
                    log.error("======= 保存告警失败  ======", e);
                }
            });
        }
        // 添加通知信息
        taskNoticeService.pushTaskSaveNotice(entity.getTaskCode(), entity.getStartTime(), entity.getEndTime(), entity.getLocation());
        this.updatePlanItemExecuteStatus(entity);
        return entity.getId();
    }

    @Override
    public Serializable updateTask(Serializable id, Task entity) {
        Task existEntity = this.selectExtensionById(id);
        // 获取执行状态
        String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(existEntity);
        // 已取消、已完成状态不允许修改
        if (TaskUtil.executeStatusValid(taskExecuteStatus)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "已取消、已完成状态的数据不允许修改");
        }
        entity.setId(id);
        // 是否升级版本
        boolean boolUpVersion = false;
        // 如果当前任务是待提交，并且点击的提交按钮，则将状态变成待派发
        if (TaskUtil.executeStatusDtj(taskExecuteStatus)) {
            if (entity.getSubmitType().equals(TaskConstants.SUBMIT_YES)) {
                entity.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(entity.getId(), TaskExecuteStatusEnum.DPF.getCode()));
                // 确认时间
                entity.setConfirmTime(LocalDateTime.now());
                // 保存预警，返回的是业务ID对应的告警类型
                Map<String, Integer> alarmTypeMap = this.carrierReleaseAdvance(entity);
                entity.setAdvanceStatus(alarmTypeMap.get(entity.getId().toString()));
            }
        }
        // 待派发、执行中、已反馈状态修改时间需要复制一条数据
        else if (TaskUtil.executeStatusDpf(taskExecuteStatus) || TaskUtil.executeStatusZxz(taskExecuteStatus) || TaskUtil.executeStatusYfk(taskExecuteStatus)) {
            // 只要停电时间或者送电时间有变更，则需要升级版本
            if (!existEntity.getStartTime().isEqual(entity.getStartTime()) || !existEntity.getEndTime().isEqual(entity.getEndTime())) {
                // 数据库老的数据
                Task oldTask = this.selectExtensionById(entity.getId());
                Serializable planItemId = oldTask.getPlanItemId();
                if (planItemId != null) {
                    // 清空老数据的计划id
                    this.lambdaUpdate().eq(Task::getId, oldTask.getId()).set(Task::getPlanItemId, null).update();
                    // 修改之前的计划项为 未关联
                    planItemService.lambdaUpdate()
                            .eq(PlanItem::getId, planItemId)
                            .set(PlanItem::getExecuteStatus, PlanItemExecuteStatusEnum.WGL.getCode())
                            .update();
                }
                // 复制任务数据
                Task task = new Task();
                task.copy(oldTask);
                task.copy(entity);
                task.setId(null);
                task.setTaskCode(null);
                // 关联单号如果为空, 则取原任务编号，否则关联单号都不变
                String assTaskCode = existEntity.getAssTaskCode();
                if (StringUtils.isBlank(assTaskCode)) {
                    assTaskCode = existEntity.getTaskCode();
                }
                task.setAssTaskCode(assTaskCode);
                // 新版本号
                task.setVersion(TaskUtil.getNewVersion(task.getVersion()));
                id = this.saveTask(task);
                // 原记录时间恢复成修改之前的
                entity.setStartTime(oldTask.getStartTime());
                entity.setEndTime(oldTask.getEndTime());
                boolUpVersion = true;
            }
        } else {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "未知状态，不允许修改");
        }
        // 如果不需要更新版本，则需要更新用户
        if (!boolUpVersion) {
            // 更新停电通知用户
            taskUserService.updateTaskUser(entity, entity.getTaskUserList());
            // 重新查询一下通知用户，因为前端不会传已反馈那些字段，所以这里需要重新查询
            entity.setTaskUserList(taskUserService.searchListByTaskId(entity.getId()));
            // 计算数量
            TaskUtil.calQty(entity);
        }
        // 更新原数据
        this.updateById(entity);
        // 更新附件id
        List<CommonFile> commonFileList = entity.getCommonFileList();
        if (!CollectionUtils.isEmpty(commonFileList)) {
            commonFileService.updateBusinessId(commonFileList, BusinessTypeEnum.TASK_MODEL.getCode(), id);
        }
        // 发送停电通知变更通知
        taskNoticeService.pushTaskUpdateNotice(existEntity.getTaskCode());
        // 如果是提交，修改本次关联的状态
        // 获取更新后的数据并判断是否需要更新停电计划状态
        Task afterUpdate = lambdaQuery().eq(Task::getId, id).one();
        this.updatePlanItemExecuteStatus(afterUpdate);
        return id;
    }

    @Override
    public Map<String, Object> validRepeatPowerCut(Task entity) {
        // 错误信息
        List<String> errMsgList = Lists.newArrayList();
        // 停电时间
        if (ObjectUtils.isEmpty(entity.getStartTime())) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电时间不能为空");
        }
        List<TaskUser> taskUserList = entity.getTaskUserList();
        if (!CollectionUtils.isEmpty(taskUserList)) {
            // 只需要校验新增的数据
            List<TaskUser> addTaskUserList = taskUserList.stream().filter(e -> e.getId() == null).collect(Collectors.toList());
            // 根据户号先查询数据库已存在的用户
            List<String> accountNumberList = addTaskUserList.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList());
            List<TaskUser> taskUserDbList = taskUserService.searchTaskUserList(accountNumberList);
            // 是否是重复停电
            taskUserService.repeatPowerCutAdvance(addTaskUserList, taskUserDbList, entity, true);
            // 获取重复停电的数据
            taskUserList.forEach(item -> {
                if (item.getBoolRepeatPowerCut().equals(YesOrNoEnum.YES.getCode())) {
                    errMsgList.add(String.format("户号[%s]属于重复停电用户，最近一次停电时间[%s]", item.getAccountNumber(), item.getLastPowerCutTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN))));
                }
            });
        }
        // 数据返回
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", 0);
        if (!CollectionUtils.isEmpty(errMsgList)) {
            resultMap.put("code", SystemError.PARAM_NOT_CHECKED_ERROR.getCode());
            resultMap.put("msg", errMsgList);
        }
        return resultMap;
    }

    @Override
    public void changeTaskExecuteStatus(List<Serializable> idList, String executeStatus) {
        if (CollectionUtils.isEmpty(idList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "参数idList不能为空");
        }
        idList.forEach(id -> this.changeTaskExecuteStatus(id, executeStatus));
    }

    @Override
    public void changeTaskExecuteStatus(Serializable id, String executeStatus) {
        // 查询此停电任务单的状态
        Task task = this.lambdaQuery().eq(Task::getId, id).one();
        if (task != null && task.getExecuteStatusId() != null) {
            TaskExecuteStatus status = taskExecuteStatusService.lambdaQuery().eq(TaskExecuteStatus::getId, task.getExecuteStatusId()).one();
            if (status != null && status.getExecuteStatus() != null) {
                // 修改任务表
                boolean result = this.lambdaUpdate()
                        .set(Task::getExecuteStatusId, taskExecuteStatusService.saveTaskExecuteStatus(id, executeStatus))
                        .set(Task::getAttribute4, status.getExecuteStatus())
                        .eq(Task::getId, id).update();
                if (!result) {
                    throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务表[%s]不存在", id));
                }
            }
        }
    }

    @Override
    public void cancelTask(TaskCancelCommand taskCancelCommand) {
        List<Serializable> taskIdList = taskCancelCommand.getTaskIdList();
        for (Serializable taskId : taskIdList) {
            // 获取停电任务
            Task entity = this.selectExtensionById(taskId);
            // 通知用户
            List<TaskUser> taskUserList = taskUserService.searchListByTaskId(taskId);
            // 默认状态已取消
            String executeStatus = TaskExecuteStatusEnum.YQX.getCode();
            // 是否更新通知用户
            boolean boolUpUser = false;
            // 需要派发取消通知
            if (taskCancelCommand.getBoolAssignCancelNotice() != null
                    && taskCancelCommand.getBoolAssignCancelNotice().equals(CommonConstants.ENABLE)) {
                // 待派发不能派发取消通知
                if (TaskUtil.executeStatusDpf(executeStatus)) {
                    throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电通知编号[%s]待派发状态取消时不允许派发取消通知操作", entity.getTaskCode()));
                }
                // 通知用户
                taskUserList.forEach(item -> {
                    // 未派发
                    item.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.WPF.getCode());
                    // 如果派发状态是未派发或者不派发，则取消通知时也默认不派发
                    if (TaskUserAssignStatusEnum.WPF.getCode().equals(item.getAssignStatus()) || TaskUserAssignStatusEnum.BPF.getCode().equals(item.getAssignStatus())) {
                        item.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.BPF.getCode());
                    }
                    // 未签
                    item.setCancelFeedbackStatus(TaskUserCancelFeedbackStatusEnum.WQ.getCode());
                });
                boolUpUser = true;
            }
            // 设置执行状态
            entity.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(taskId, executeStatus));
            // 更新用户表数据
            if (boolUpUser) {
                entity.setTaskUserList(taskUserList);
                TaskUtil.calQty(entity);
                taskUserService.updateBatchById(taskUserList);
            }
            // 取消时间
            entity.setCancelTime(LocalDateTime.now());
            // 只保存 取消前的任务单的非已取消的状态
            if (!taskCancelCommand.getExecuteStatus().equals(TaskExecuteStatusEnum.YQX.getCode())) {
                entity.setAttribute4(taskCancelCommand.getExecuteStatus());
            }
            // 修改通知表
            boolean result = this.updateById(entity);
            // 发送取消通知
            taskNoticeService.pushTaskCancelNotice(entity.getTaskCode());
            if (!result) {
                throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电通知表[%s]不存在", taskId));
            }
        }
    }

    @Override
    public void revokeCancelTask(TaskCancelCommand taskCancelCommand) {
        List<Serializable> taskIdList = taskCancelCommand.getTaskIdList();

        for (Serializable taskId : taskIdList) {
            // 获取取消前状态
            Task task = this.lambdaQuery().eq(Task::getId, taskId).one();
            // 已取消的计划单再取消时不变更任何数据状态
            if (task.getAttribute4() != null && !task.getAttribute4().equals(TaskExecuteStatusEnum.YQX.getCode())) {
                // 获取停电任务 重置并更新到取消前的状态
                Serializable executeStatusId = taskExecuteStatusService.saveTaskExecuteStatus(taskId, task.getAttribute4());
                this.lambdaUpdate().set(Task::getCancelTime, null)
                        .set(Task::getExecuteStatusId, executeStatusId).eq(Task::getId, taskId).update();
                // 查询通知用户 重置并更新到取消前的状态
                List<TaskUser> taskUserList = taskUserService.searchListByTaskId(taskId);
                for (TaskUser taskUser : taskUserList) {
                    taskUserService.lambdaUpdate().set(TaskUser::getCancelReason, null).set(TaskUser::getCancelAssignStatus, null)
                            .set(TaskUser::getCancelAssignMethod, null).set(TaskUser::getCancelFeedbackStatus, null)
                            .set(TaskUser::getCancelFeedbackTime, null).eq(TaskUser::getId, taskUser.getId()).update();
                }
            } else {
                throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电通知表[%s]取消前任务状态不存在", taskId));
            }
        }
    }

    @Override
    public List<CommonFile> batchUpload(MultipartFile[] fileList) {
        return commonFileService.batchUpload(fileList);
    }

    @Override
    public TaskTotalResource getTaskTotal(TaskTotalQueryCommand command) {
        // 统计信息对象
        TaskTotalResource totalResource = new TaskTotalResource();
        // 待提交数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.DTJ.getCode()));
        totalResource.setSubmitQty(this.baseMapper.selectTaskTotal(command));
        // 待派发数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.DPF.getCode()));
        totalResource.setReleaseQty(this.baseMapper.selectTaskTotal(command));
        // 执行中数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.ZXZ.getCode()));
        totalResource.setExecutionQty(this.baseMapper.selectTaskTotal(command));
        // 已取消数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YQX.getCode()));
        totalResource.setCancelQty(this.baseMapper.selectTaskTotal(command));
        // 已反馈数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YFK.getCode()));
        totalResource.setFeedbackQty(this.baseMapper.selectTaskTotal(command));
        // 已完成数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YWC.getCode()));
        totalResource.setFinishedQty(this.baseMapper.selectTaskTotal(command));

        return totalResource;
    }

    @Override
    public TaskSumTotalResource getTaskSumTotal(TaskTotalQueryCommand command) {
        // 统计信息对象
        TaskSumTotalResource sumTotalResource = new TaskSumTotalResource();
        // 停电通知总数量
        sumTotalResource.setTaskSumQty(this.baseMapper.selectTaskTotal(command));
        // 已完成通知数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YWC.getCode()));
        sumTotalResource.setTaskFinishedQty(this.baseMapper.selectTaskTotal(command));
        // 已取消通知数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YQX.getCode()));
        sumTotalResource.setTaskCancelQty(this.baseMapper.selectTaskTotal(command));
        // 停电用户相关统计
        TaskUserTotalQueryCommand taskUserCommand = new TaskUserTotalQueryCommand();
        taskUserCommand.setCreateDateBegin(command.getCreateDateBegin());
        taskUserCommand.setCreateDateEnd(command.getCreateDateEnd().plusDays(-1));
        sumTotalResource.setTaskUserSumTotalResource(taskUserService.getTaskUserSumTotal(taskUserCommand));
        return sumTotalResource;
    }

    @Override
    public Map<String, Integer> carrierReleaseAdvance(Task entity) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            if (entity == null) {
                throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "停电任务通知单不存在，请检查");
            }
            // 获取下达风险预警规则
            TaskAdvanceRuleResource ruleResource = advanceRuleFeignClient.getCarrierReleaseRule().getData();
            if (ObjectUtils.isEmpty(ruleResource)) {
                throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "未查询到下达风险预警规则数据，请检查");
            }
            // 预警规则配置的天数
            Integer days = ruleResource.getDays();
            // 计算确认时间跟停电时间的天数
            LocalDateTime startTime = entity.getStartTime();
            LocalDateTime confirmTime = entity.getConfirmTime();
            int daysNum = (int) (startTime.toLocalDate().toEpochDay() - confirmTime.toLocalDate().toEpochDay());
            // 如果天数大于阈值  不需要预警
            if (daysNum > days) {
                return resultMap;
            }
            // 保存下达风险预警
            List<AdvanceParamCommand> addAdvanceList = Lists.newArrayList();
            addAdvanceList.add(AdvanceParamCommand.builder().businessId(entity.getId()).businessCode(entity.getTaskCode())
                    .taskCode(entity.getTaskCode()).startTime(entity.getStartTime()).endTime(entity.getEndTime()).days(days).build());
            resultMap = advanceNoticeFeignClient.saveCarrierReleaseAdvance(AdvanceNoticeSaveCommand.builder()
                    .businessType(BusinessTypeEnum.TASK_MODEL.getCode()).businessList(addAdvanceList).days(days).build()).getData();
        } catch (Exception e) {
            log.error("======= 下达风险预警保存失败  ======", e);
        }
        return resultMap;
    }

    @Override
    public void deleteTask(List<Serializable> taskIdList) {
        for (Serializable taskId : taskIdList) {
            // 获取停电通知
            Task entity = this.selectExtensionById(taskId);
            // 获取当前通知的执行状态
            String executeStatus = TaskUtil.getTaskExecuteStatus(entity);
            // 如果是待提交状态进行取消，则直接物理删除
            if (TaskUtil.executeStatusDtj(executeStatus)) {
                boolean result = this.getBaseMapper().deleteTask(taskId);
                if (result) {
                    // 物理删除执行状态
                    taskExecuteStatusService.deleteByTaskId(taskId);
                    // 物理删除停电用户
                    taskUserService.deleteByTaskId(taskId);
                }
            } else {
                this.removeById(taskId);
            }
        }
    }

    @Override
    public void download(List<Task> dataList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (Task task : dataList) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            map.put("任务编号", task.getTaskCode());
            map.put("单位名称", task.getUnitName());
            map.put("停电类型", task.getType());
            map.put("停电原因", task.getReason());
            map.put("停电时间", task.getStartTime());
            map.put("送电时间", task.getEndTime());
            map.put("执行状态", task.getTaskExecuteStatus().getExecuteStatus());
            map.put("变电站名称", task.getStationName());
            map.put("线路名称", task.getLineName());
            map.put("设备名称", task.getDeviceName());
            map.put("停电范围", task.getRanges());
            map.put("停电位置", task.getLocation());
            map.put("工作内容", task.getJobContent());
            map.put("是否通知媒体", task.getBoolNotifyMedia());
            map.put("任务派发用户姓名", task.getAssignedByName());
            map.put("通知来源", task.getTaskSource());
            map.put("告警状态", task.getAdvanceStatus());
            map.put("确认时间", task.getConfirmTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void insertOrSaveBatch(List<TaskOutSaveCommand> taskOutSaveCommandList) {
        // 获取停电原因
        List<SysLovLine> sysLovLineList = sysLovLineService.lambdaQuery().in(SysLovLine::getLovId, Lists.newArrayList(5, 6, 9)).eq(SysLovLine::getStatus, 1).list();
        for (TaskOutSaveCommand outSaveCommand : taskOutSaveCommandList) {
            if (StringUtils.isBlank(outSaveCommand.getOutId())) {
                throw BusinessException.build(String.format("[%s-%s]outId为空，数据非法", outSaveCommand.getStationName(), outSaveCommand.getLineName()));
            }
            Task task = new Task();
            // 停电类型
            task.setType(1);
            // 是否通知媒体
            task.setBoolNotifyMedia(0);
            // 单位名称
            task.setUnitName(TaskConstants.UNIT_NAME);
            // 状态
            task.setStatus(1);
            // 删除标记
            task.setDeleted(0);
            task.setSubmitType(TaskConstants.SUBMIT_YES);
            BeanUtils.copyProperties(outSaveCommand, task);
            String reason = outSaveCommand.getReason();
            if (StringUtils.isBlank(reason)){
                reason = "配合市政工程";
            }
            // 获取相应的停电原因
            String finalReason = reason;
            SysLovLine reasonLovLine = sysLovLineList.stream()
                    .filter(t -> t.getLovId().equals(5L) && t.getName().equals(finalReason))
                    .findFirst().orElse(null);
            // 如果停电原因不存在，则新增一条停电原因
            if (ObjectUtils.isEmpty(reasonLovLine)) {
                reasonLovLine = new SysLovLine();
                reasonLovLine.setLovId(5L);
                reasonLovLine.setName(reason);
                reasonLovLine.setValue(String.valueOf(sysLovLineList.stream().filter(t -> t.getLovId().equals(5L)).count() + 1));
                reasonLovLine.setCode(String.valueOf(sysLovLineList.stream().filter(t -> t.getLovId().equals(5L)).count() + 1));
                reasonLovLine.setSort((int) sysLovLineList.stream().filter(t -> t.getLovId().equals(5L)).count() + 1);
                sysLovLineService.save(reasonLovLine);
            }
            task.setReason(Integer.valueOf(reasonLovLine.getCode()));
            // 获取停电通知用户
            List<TaskUserOutSaveCommand> userOutSaveCommandList = outSaveCommand.getTaskUserOutSaveCommandList();
            List<TaskUser> taskUserList = Lists.newArrayList();
            for (TaskUserOutSaveCommand taskUserOutSaveCommand : userOutSaveCommandList) {
                TaskUser taskUser = new TaskUser();
                String userPriority = taskUserOutSaveCommand.getUserPriority();
                String userType = taskUserOutSaveCommand.getUserType();
                BeanUtils.copyProperties(taskUserOutSaveCommand, taskUser);
                if (StringUtils.isBlank(taskUserOutSaveCommand.getVoltageLevel())) {
                    taskUser.setVoltageLevel("交流220V");
                }
                if (taskUserOutSaveCommand.getBoolShortTime() == null) {
                    taskUser.setBoolShortTime(1);
                }
                if (StringUtils.isBlank(taskUserOutSaveCommand.getUserPriority())) {
                    taskUser.setUserPriority(1);
                }
                if (StringUtils.isBlank(taskUserOutSaveCommand.getUserType())) {
                    taskUser.setUserType(4);
                }
                // SM4解密用户姓名，地址，手机号
                this.decryptTaskUserInfo(taskUser);
                // 获取用户重要性
                sysLovLineList.stream()
                        .filter(t -> t.getLovId().equals(6L) && t.getName().equals(userPriority))
                        .findFirst()
                        .ifPresent(t -> taskUser.setUserPriority(Integer.valueOf(t.getCode())));
                // 获取用户类型
                sysLovLineList.stream()
                        .filter(t -> t.getLovId().equals(9L) && t.getName().equals(userType))
                        .findFirst()
                        .ifPresent(t -> taskUser.setUserType(Integer.valueOf(t.getCode())));
                // 是否短时停电
                taskUser.setBoolShortTime("是".equals(taskUserOutSaveCommand.getBoolShortTime()) ? 1 : 0);
                // 用户
                taskUserList.add(taskUser);
            }
            task.setTaskUserList(taskUserList);
            // 判断更新还是新增
            Task existTask = this.lambdaQuery()
                    .eq(Task::getOutId, outSaveCommand.getOutId())
                    .eq(Task::getStatus, 1)
                    .one();
            this.insertOrUpdateForOut(existTask, task);
        }
        log.info("return");
    }

    private void insertOrUpdateForOut(Task existTask, Task task) {
        if (ObjectUtils.isNotEmpty(existTask)) {
            task.setSubmitType(0);
            task.setType(1);
            task.setBoolNotifyMedia(0);
            task.setUnitName(TaskConstants.UNIT_NAME);
            task.setStatus(1);
            task.setDeleted(0);
            task.setSubmitType(TaskConstants.SUBMIT_YES);
            task.setId(existTask.getId());
            this.lambdaUpdate().eq(Task::getId, existTask.getId()).update(task);
            List<TaskUser> taskUserList = task.getTaskUserList();
            taskUserService.deleteByTaskId(existTask.getId());
            taskUserList.forEach(t -> {
                t.setTaskId(existTask.getId());
                if (StringUtils.isBlank(t.getVoltageLevel())) {
                    t.setVoltageLevel("交流220V");
                }
                if (t.getBoolShortTime() == null) {
                    t.setBoolShortTime(1);
                }
            });
            taskUserService.saveTaskUser(task, taskUserList);
        } else {
            this.saveTask(task);
        }
    }

    /**
     * 更新停电通知对应的停电计划状态
     *
     * @param task 停电通知
     */
    private void updatePlanItemExecuteStatus(Task task) {
        // 如果是确认的话，需要改变相应的停电计划状态
        if (TaskConstants.SUBMIT_YES.equals(task.getSubmitType())) {
            Serializable planItemId = task.getPlanItemId();
            // 检索改停电计划项目是否已经被关联，如果被关联则无法关联
            PlanItem planItem = planItemService.getById(planItemId);
            if (ObjectUtils.isNotEmpty(planItem) && PlanItemExecuteStatusEnum.YGL.getCode().equals(planItem.getExecuteStatus())) {
                throw BusinessException.build("该停电计划项已被其他停电通知关联");
            }
            if (planItemId != null) {
                planItemService.lambdaUpdate()
                        .eq(PlanItem::getId, planItemId)
                        .set(PlanItem::getExecuteStatus, PlanItemExecuteStatusEnum.YGL.getCode())
                        .update();
            }
        }
    }

    private void decryptTaskUserInfo(TaskUser taskUser) {
        SM4 sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding, Str2HexUtil.generate16BytesKey(SM4_KEY));
        // SM4解密用户姓名，地址，手机号
        String customerName = taskUser.getCustomerName();
        customerName = StringUtils.isNotBlank(customerName) ? Str2HexUtil.binToStr(sm4.decrypt(customerName)) : "";
        String customerAddress = taskUser.getCustomerAddress();
        customerAddress = StringUtils.isNotBlank(customerAddress) ? Str2HexUtil.binToStr(sm4.decrypt(customerAddress)) : "";
        String phone = taskUser.getPhone();
        phone = StringUtils.isNotBlank(phone) ? Str2HexUtil.binToStr(sm4.decrypt(phone)) : "";
        log.info("customerName : {}", customerName);
        log.info("customerAddress : {}", customerAddress);
        log.info("phone : {}", phone);
        taskUser.setCustomerName(customerName);
        taskUser.setCustomerAddress(customerAddress);
        taskUser.setPhone(phone);
    }
}