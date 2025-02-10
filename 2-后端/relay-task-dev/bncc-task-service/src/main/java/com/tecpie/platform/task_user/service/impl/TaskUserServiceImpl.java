package com.tecpie.platform.task_user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.TaskConstants;
import com.tecpie.platform.common.enums.*;
import com.tecpie.platform.common.feign.advance.AdvanceNoticeFeignClient;
import com.tecpie.platform.common.feign.advanceRule.AdvanceRuleFeignClient;
import com.tecpie.platform.common.qrcode.ZXingCode;
import com.tecpie.platform.common.util.LunarCalendar;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.save.AdvanceParamCommand;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import com.tecpie.platform.seq.service.SeqGenService;
import com.tecpie.platform.task.api.resource.EngineersSumResource;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskExecuteStatusService;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.platform.task_notice.service.TaskNoticeService;
import com.tecpie.platform.task_user.api.command.query.EngineerSummaryQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserAssignSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserPrintResource;
import com.tecpie.platform.task_user.api.resource.TaskUserSumTotalResource;
import com.tecpie.platform.task_user.api.resource.TaskUserTotalResource;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.entity.TaskUserFeedbackLog;
import com.tecpie.platform.task_user.mapper.TaskUserMapper;
import com.tecpie.platform.task_user.service.TaskUserBackupPhoneService;
import com.tecpie.platform.task_user.service.TaskUserFeedbackLogService;
import com.tecpie.platform.task_user.service.TaskUserService;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
import com.tecpie.platform.user.business.system.data.service.SysLovService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 停电任务用户表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskUserServiceImpl extends BaseServiceImpl<TaskUserMapper, TaskUser> implements TaskUserService {

    private final TaskService taskService;
    private final SeqGenService seqGenService;
    private final TaskExecuteStatusService taskExecuteStatusService;
    private final SysLovService sysLovService;
    private final AdvanceRuleFeignClient advanceRuleFeignClient;
    private final AdvanceNoticeFeignClient advanceNoticeFeignClient;
    private final TaskUserFeedbackLogService taskUserFeedbackLogService;
    private final TaskUserBackupPhoneService taskUserBackupPhoneService;
    private final TaskNoticeService taskNoticeService;
    private final SysLovLineService sysLovLineService;

    @Lazy
    public TaskUserServiceImpl(TaskService taskService,
                               SeqGenService seqGenService,
                               TaskExecuteStatusService taskExecuteStatusService,
                               SysLovService sysLovService,
                               AdvanceRuleFeignClient advanceRuleFeignClient,
                               AdvanceNoticeFeignClient advanceNoticeFeignClient,
                               TaskUserFeedbackLogService taskUserFeedbackLogService,
                               TaskUserBackupPhoneService taskUserBackupPhoneService,
                               TaskNoticeService taskNoticeService,
                               SysLovLineService sysLovLineService) {
        this.taskService = taskService;
        this.seqGenService = seqGenService;
        this.taskExecuteStatusService = taskExecuteStatusService;
        this.sysLovService = sysLovService;
        this.advanceRuleFeignClient = advanceRuleFeignClient;
        this.advanceNoticeFeignClient = advanceNoticeFeignClient;
        this.taskUserFeedbackLogService = taskUserFeedbackLogService;
        this.taskUserBackupPhoneService = taskUserBackupPhoneService;
        this.taskNoticeService = taskNoticeService;
        this.sysLovLineService = sysLovLineService;
    }

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    @Override
    public TaskUser selectExtensionById(Serializable id) {
        TaskUser entity = this.baseMapper.selectExtensionById(id);
        Serializable engineersTeamId = TaskUtil.getEngineersTeamId();
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务用户表[%s]不存在", id));
        }
        // 外网环境用户查询时，通知单和用户都必须绑定工程队
        if (systemContainer == 1 && (engineersTeamId == null || entity.getEngineersTeamId() == null || "".equals(engineersTeamId))) {
            throw BusinessException.build(SystemError.ACCESS_DENIED_ERROR, "当前用户或通知单未绑定工程队");
        }
        // 外网环境查询工程队时，通知单工程队进行和账号绑定的工程队进行匹配
        if (systemContainer == 1 && !entity.getEngineersTeamId().equals(engineersTeamId)) {
            throw BusinessException.build(SystemError.ACCESS_DENIED_ERROR, "当前用户绑定工程队无法查看停电通知用户");
        }
        // 获取本次停电时间
        LocalDateTime startTime = entity.getTask().getStartTime();
        // 获取上次停电时间
        LocalDateTime lastPowerCutTime = entity.getLastPowerCutTime();
        if (startTime != null && lastPowerCutTime != null) {
            // 计算和上次停电的间隔天数
            entity.setIntervalDays((int) Duration.between(lastPowerCutTime, startTime).toDays());
            // 计算距离停电时长
            entity.setDistanceDuration((int) (startTime.toLocalDate().toEpochDay() - LocalDateTime.now().toLocalDate().toEpochDay()));
        }
        return entity;
    }

    @Override
    public List<TaskUser> searchExtensionPageList(TaskUserQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<TaskUser> searchListByTaskId(Serializable taskId) {
        TaskUserQueryCommand command = new TaskUserQueryCommand();
        command.setTaskId(taskId);
        return this.searchExtensionPageList(command);
    }

    @Override
    public Map<String, String> selectMaxFeedbackTime(List<Serializable> taskUserIdList) {
        Map<String, String> resultMap = Maps.newHashMap();
        List<TaskUserFeedbackLog> feedbackLogList = taskUserFeedbackLogService.selectMaxFeedbackTimeList(taskUserIdList);
        if (!CollectionUtils.isEmpty(feedbackLogList)) {
            feedbackLogList.forEach(item -> resultMap.put(item.getTaskUserId().toString(), item.getCreateDate().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))));
        }
        return resultMap;
    }

    @Override
    public List<TaskUserPrintResource> getPrintData(List<Serializable> taskUserIdList) {
        if (CollectionUtils.isEmpty(taskUserIdList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务用户taskUserIdList不能为空");
        }
        // 查询所有的停电用户
        TaskUserQueryCommand command = new TaskUserQueryCommand();
        command.setTaskUserIdList(taskUserIdList);
        List<TaskUser> taskUserList = this.baseMapper.searchExtensionPageList(command);
        // 获取所有TaskId，从而获取所有Task
        List<Serializable> taskIdList = taskUserList.stream().map(TaskUser::getTaskId).distinct().collect(Collectors.toList());
        List<Task> taskList = taskService.lambdaQuery().in(Task::getId, taskIdList).list();
        if (CollectionUtils.isEmpty(taskUserList)) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, "未查询到匹配的停电任务用户数据");
        }
        // 获取所有停电原因
        List<SysLovLine> reasonList = sysLovLineService.lambdaQuery().eq(SysLovLine::getLovId, 5L).list();
        return this.handlePrintData(taskUserList, taskList, reasonList);
    }

    @Override
    public List<TaskUserPrintResource> getPrintData(TaskUserQueryCommand command) {
        if (ObjectUtils.isEmpty(command.getTaskId())) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务ID[taskId]不能为空");
        }
        // 停电任务
        Task task = taskService.selectExtensionById(command.getTaskId());
        // 获取所有停电原因
        List<SysLovLine> reasonList = sysLovLineService.lambdaQuery().eq(SysLovLine::getLovId, 5L).list();
        return this.handlePrintData(this.baseMapper.searchExtensionPageList(command), List.of(task), reasonList);
    }

    /**
     * 处理打印数据
     */
    private List<TaskUserPrintResource> handlePrintData(List<TaskUser> taskUserList, List<Task> taskList, List<SysLovLine> reasonList) {
        // 返回的数据
        List<TaskUserPrintResource> taskUserPrintList = Lists.newArrayList();
        // Calendar日期对象
        Calendar calendar = Calendar.getInstance();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH时mm分");
        // 循环用户
        taskUserList.forEach(e -> {
            TaskUserPrintResource taskUserPrintResource = new TaskUserPrintResource();
            // 复制任务数据
            BeanUtil.copyProperties(e.getTask(), taskUserPrintResource, CopyOptions.create().setIgnoreNullValue(true));
            // 复制任务用户数据
            BeanUtil.copyProperties(e, taskUserPrintResource, CopyOptions.create().setIgnoreNullValue(true));
            // 停电时间
            LocalDateTime startTime = taskUserPrintResource.getStartTime();
            calendar.setTime(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
            LunarCalendar startLunar = new LunarCalendar(calendar);
            // 外部id，配网办id
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(taskList)) {
                taskList.stream().filter(t -> t.getId().equals(e.getTaskId())).findFirst().ifPresent(t -> {
                    taskUserPrintResource.setOutId(t.getOutId());
                    reasonList.stream()
                            .filter(reason -> reason.getCode().equals(String.valueOf(t.getReason())))
                            .findFirst()
                            .ifPresent(reason -> taskUserPrintResource.setReason(reason.getName()));
                });
            }
            // 星期几
            taskUserPrintResource.setStartWeek(startLunar.weekConvert(calendar));
            // 时分
            taskUserPrintResource.setStartHourTime(startTime.format(pattern));
            // 送时间
            LocalDateTime endTime = taskUserPrintResource.getEndTime();
            calendar.setTime(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
            LunarCalendar endLunar = new LunarCalendar(calendar);
            // 星期几
            taskUserPrintResource.setEndWeek(endLunar.weekConvert(calendar));
            // 时分
            taskUserPrintResource.setEndHourTime(endTime.format(pattern));
            // 生成二维码
            try {
                InputStream inputStream = this.genTaskQrCode(e);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                taskUserPrintResource.setQrCode(base64);
            } catch (IOException ex) {
                throw BusinessException.build("二维码生成失败");
            }
            taskUserPrintList.add(taskUserPrintResource);
        });
        return taskUserPrintList;
    }

    @Override
    public void saveTaskUser(Task task, List<TaskUser> list) {
        if (!CollectionUtils.isEmpty(list)) {
            // 户号筛选出来
            List<String> accountNumberList = list.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList());
            // 获取所有户号对应的备用联系方式
            Map<String, String> backupPhoneMap = taskUserBackupPhoneService.searchListByAccountNumber(accountNumberList);
            // 根据户号先查询数据库已存在的用户
            List<TaskUser> taskUserDbList = this.baseMapper.searchTaskUserList(accountNumberList);
            list.forEach(item -> {
                item.setId(null);
                preSaveData(item, task.getId(), backupPhoneMap);
            });
            this.saveBatch(list);
            // 异步调用告警信息
            ThreadUtil.execAsync(() -> {
                try {
                    // 如果是重复停电，则保存告警
                    this.repeatPowerCutAdvance(list, taskUserDbList, task, true);
                } catch (Exception e) {
                    log.error("======= 保存告警失败  ======", e);
                }
            });
        }
    }

    @Override
    public void updateTaskUser(Task task, List<TaskUser> list) {
        // 保存或更新停电通知用户
        if (!CollectionUtils.isEmpty(list)) {
            // 获取所有户号对应的备用联系方式
            Map<String, String> backupPhoneMap = taskUserBackupPhoneService.searchListByAccountNumber(list.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList()));
            // 需要新增的用户数据
            List<TaskUser> addTaskUserList = Lists.newArrayList();
            list.forEach(item -> {
                if (ObjectUtils.isEmpty(item.getId())) {
                    preSaveData(item, task.getId(), backupPhoneMap);
                    addTaskUserList.add(item);
                }
            });
            List<TaskUser> taskUserDbList;
            if (!CollectionUtils.isEmpty(addTaskUserList)) {
                // 根据新增的户号先查询数据库已存在的用户
                taskUserDbList = this.baseMapper.searchTaskUserList(addTaskUserList.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList()));
            } else {
                taskUserDbList = Lists.newArrayList();
            }
            // 级联操作
            this.cascadeUpdate(this.searchListByTaskId(task.getId()), list);
            // 异步调用重复停电告警信息
            if (!CollectionUtils.isEmpty(addTaskUserList) && !CollectionUtils.isEmpty(taskUserDbList)) {
                ThreadUtil.execAsync(() -> {
                    try {
                        this.repeatPowerCutAdvance(addTaskUserList, taskUserDbList, task, true);
                    } catch (Exception e) {
                        log.error("======= 保存重复停电告警失败  ======", e);
                    }
                });
            }
        }
    }


    private void preSaveData(TaskUser taskUser, Serializable taskId, Map<String, String> backupPhoneMap) {
        taskUser.setTaskId(taskId);
        // 备用联系方式
        if (StringUtils.isBlank(taskUser.getBackupPhone())) {
            taskUser.setBackupPhone(backupPhoneMap.get(taskUser.getAccountNumber()));
        }
        taskUser.setReceiptCode(seqGenService.getValue(TaskConstants.TASK_USER_CODE_SEQ));
        taskUser.setAssignStatus(TaskUserAssignStatusEnum.WPF.getCode());
        taskUser.setFeedbackStatus(TaskUserFeedbackStatusEnum.WQ.getCode());
    }

    /**
     * 处理用户信息
     */
    private Task handTaskUser(List<Serializable> taskUserIdList) {
        if (CollectionUtils.isEmpty(taskUserIdList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务用户IdList不能为空");
        }
        // 停电任务用户List
        List<TaskUser> taskUserList = this.lambdaQuery().in(TaskUser::getId, taskUserIdList).list();
        // 停电任务对象
        Task task = null;
        if (!CollectionUtils.isEmpty(taskUserList)) {
            task = taskService.selectExtensionById(taskUserList.get(0).getTaskId());
        }
        assert task != null;
        task.setTaskUserList(taskUserList);
        return task;
    }

    @Override
    public void batchAssign(TaskUserAssignSaveCommand command) {
        // 获取任务
        Task task = handTaskUser(command.getTaskUserIdList());
        // 用户派发
        this.userAssign(task, command.getEngineersTeamId(), false);
    }

    @Override
    public void oneTouchAssign(TaskUserQueryCommand command) {
        if (ObjectUtils.isEmpty(command.getTaskId())) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务ID[taskId]不能为空");
        }
        // 工程队
        Serializable engineersTeamId = command.getEngineersTeamId();
        // 停电任务
        Task task = taskService.selectExtensionById(command.getTaskId());
        // 这里需要清空工程队ID，不然会根据这个字段进行查询
        command.setEngineersTeamId(null);
        // 根据查询条件获取停电通知用户List
        task.setTaskUserList(this.baseMapper.searchExtensionPageList(command));
        // 用户派发
        this.userAssign(task, engineersTeamId, true);
    }

    /**
     * 用户派发
     */
    private void userAssign(Task task, Serializable engineersTeamId, boolean isOneTouch) {
        // 当前停电任务执行状态
        String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(task);
        if (TaskUtil.executeStatusDtj(taskExecuteStatus) || TaskUtil.executeStatusYfk(taskExecuteStatus) || TaskUtil.executeStatusYwc(taskExecuteStatus)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "待提交、已反馈、已完成的数据不能进行派发操作");
        }
        // 是否是已取消
        boolean boolYqx = TaskUtil.executeStatusYqx(taskExecuteStatus);
        // 停电任务用户
        List<TaskUser> taskUserList = task.getTaskUserList();
        if (CollectionUtils.isEmpty(taskUserList)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "未查询到可派发的用户数据，不能进行派发操作");
        }
        // 是否是取消通知派发
        for (TaskUser taskUser : taskUserList) {
            // 已取消
            if (boolYqx) {
                // 取消通知如果是已派发，则不能重复派发
                if (!TaskUserCancelAssignStatusEnum.WPF.getCode().equals(taskUser.getCancelAssignStatus())) {
                    if (isOneTouch) {
                        continue;
                    }
                    throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("任务用户[%s]取消派发状态必须是未派发的数据才能进行派发操作", taskUser.getReceiptCode()));
                }
                taskUser.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.YPF.getCode());
                continue;
            }
            // 如果是不派发、已派发、已反馈，则不能操作派发
            String assignStatus = taskUser.getAssignStatus();
            if (!TaskUserAssignStatusEnum.WPF.getCode().equals(assignStatus)) {
                if (!isOneTouch) {
                    throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("任务用户[%s]派发状态必须是未派发的数据才能进行派发操作", taskUser.getReceiptCode()));
                }
            } else {
                // 工程队ID
                if (ObjectUtils.isEmpty(engineersTeamId)) {
                    throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "当前派发没有选择工程队，不能进行派发操作");
                }
                taskUser.setAssignStatus(TaskUserAssignStatusEnum.YPF.getCode());
                taskUser.setAssignTime(LocalDateTime.now());
                taskUser.setEngineersTeamId(engineersTeamId);
            }
        }
        // 检索取消派发/正常派发是否派发过，如果派发过不进行派发，根据boolQxz决定是何种派发形式
        LambdaQueryChainWrapper<TaskUser> wrapper = this.lambdaQuery().eq(TaskUser::getTaskId, task.getId());
        if (boolYqx) {
            wrapper.in(TaskUser::getCancelAssignStatus,
                    List.of(TaskUserCancelAssignStatusEnum.YPF.getCode(), TaskUserCancelAssignStatusEnum.YFK.getCode()));
        } else {
            wrapper.in(TaskUser::getAssignStatus,
                    List.of(TaskUserAssignStatusEnum.YPF.getCode(), TaskUserAssignStatusEnum.YFK.getCode()));
        }
        boolean isPush = wrapper.count() == 0;
        // 批量更新停电用户数据
        this.updateBatchById(taskUserList);
        // 非取消通知派发并且当前任务是待派发，则停电通知状态变成执行中
        if (!boolYqx && TaskUtil.executeStatusDpf(taskExecuteStatus)) {
            // 执行状态ID
            task.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(task.getId(), TaskExecuteStatusEnum.ZXZ.getCode()));
        }
        // 重新计算数量
        taskUserList = this.searchListByTaskId(task.getId());
        task.setTaskUserList(taskUserList);
        TaskUtil.calQty(task);
        // 修改任务表
        taskService.updateById(task);
        // 发送通知
        if (isPush) {
            if (boolYqx) {
                taskNoticeService.pushAssignCancelNotice(task.getTaskCode());
            } else {
                taskNoticeService.pushAssignNotice(task.getTaskCode());
            }
        }
    }

    @Override
    public void changeEngineersTeam(TaskUserAssignSaveCommand command) {
        // 停电任务用户List
        List<TaskUser> taskUserList = this.lambdaQuery().in(TaskUser::getId, command.getTaskUserIdList()).list();
        // 变更派发负责人
        this.handleChangeEngineersTeam(taskUserList, command.getEngineersTeamId(), false);
    }

    @Override
    public void oneTouchChangeEngineersTeam(TaskUserQueryCommand command) {
        if (ObjectUtils.isEmpty(command.getTaskId())) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务ID[taskId]不能为空");
        }
        // 工程队
        Serializable engineersTeamId = command.getEngineersTeamId();
        // 这里需要清空工程队ID，不然会根据这个字段进行查询
        command.setEngineersTeamId(null);
        List<TaskUser> taskUserList = this.baseMapper.searchExtensionPageList(command);
        // 变更派发负责人
        this.handleChangeEngineersTeam(taskUserList, engineersTeamId, true);
    }

    private void handleChangeEngineersTeam(List<TaskUser> taskUserList, Serializable engineersTeamId, boolean isOneTouch) {
        if (CollectionUtils.isEmpty(taskUserList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "没有查询到符合条件的用户数据");
        }
        if (ObjectUtils.isEmpty(engineersTeamId)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "派发负责人[engineersTeamId]不能为空");
        }
        for (TaskUser entity : taskUserList) {
            if (!TaskUserAssignStatusEnum.YPF.getCode().equals(entity.getAssignStatus()) && !TaskUserAssignStatusEnum.YFK.getCode().equals(entity.getAssignStatus())) {
                if (isOneTouch) {
                    continue;
                }
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务用户[%s]派发状态必须是已派发或已反馈才能进行变更负责方", entity.getReceiptCode()));
            }
            // 工程队ID
            entity.setEngineersTeamId(engineersTeamId);
        }
        this.updateBatchById(taskUserList);
        // 重新计算数量
        Task task = taskService.selectExtensionById(taskUserList.get(0).getTaskId());
        task.setTaskUserList(this.searchListByTaskId(task.getId()));
        TaskUtil.calQty(task);
        taskService.updateById(task);
    }

    @Override
    public void cancel(List<Serializable> taskUserIdList, String cancelReason) {
        // 获取任务
        Task task = handTaskUser(taskUserIdList);
        // 处理取消派发
        this.handleCancel(task, cancelReason, false);
    }

    @Override
    public void oneTouchCancel(TaskUserQueryCommand command) {
        if (ObjectUtils.isEmpty(command.getTaskId())) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电任务ID[taskId]不能为空");
        }
        // 取消理由
        String cancelReason = command.getCancelReason();
        // 停电任务
        Task task = taskService.selectExtensionById(command.getTaskId());
        // 取消理由不做查询
        command.setCancelReason(null);
        task.setTaskUserList(this.baseMapper.searchExtensionPageList(command));
        // 处理取消派发
        this.handleCancel(task, cancelReason, true);
    }

    private void handleCancel(Task task, String cancelReason, boolean isOneTouch) {
        // 获取执行状态
        String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(task);
        List<TaskUser> taskUserList = task.getTaskUserList();
        if (CollectionUtils.isEmpty(taskUserList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "没有查询到符合条件的用户数据");
        }
        // 循环停电任务用户
        for (TaskUser taskUser : taskUserList) {
            // 执行状态是已取消
            if (TaskUtil.executeStatusYqx(taskExecuteStatus)) {
                // 取消通知如果是不派发，则不能重复取消
                if (TaskUserCancelAssignStatusEnum.BPF.getCode().equals(taskUser.getCancelAssignStatus())) {
                    if (isOneTouch) {
                        continue;
                    }
                    throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务用户[%s]取消通知已经取消，不能重复取消", taskUser.getReceiptCode()));
                }
                // 更新用户
                this.lambdaUpdate().set(TaskUser::getCancelAssignStatus, TaskUserCancelAssignStatusEnum.BPF.getCode())
                        .set(TaskUser::getCancelReason, cancelReason)
                        .set(TaskUser::getCancelAssignMethod, null)
                        .eq(TaskUser::getId, taskUser.getId()).update();
                continue;
            }
            // 如果是不派发，则不能重复取消
            if (TaskUserAssignStatusEnum.BPF.getCode().equals(taskUser.getAssignStatus())) {
                if (isOneTouch) {
                    continue;
                }
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务用户[%s]已经取消，不能重复取消", taskUser.getReceiptCode()));
            }
            // 更新用户
            this.lambdaUpdate().set(TaskUser::getAssignStatus, TaskUserAssignStatusEnum.BPF.getCode())
                    .set(TaskUser::getCancelReason, cancelReason)
                    .set(TaskUser::getAssignMethod, null)
                    .set(TaskUser::getAssignTime, null)
                    .eq(TaskUser::getId, taskUser.getId()).update();
        }
        // 重新查询所有停电用户，判断是否全部派发完成(不含不派发)，这里有可能其他用户都已反馈，最后一个用户做了取消操作，这个时候需要将任务执行状态变更为已反馈
        taskUserList = this.searchListByTaskId(task.getId());
        // 是否全部反馈
        boolean boolAllFeedback = true;
        for (TaskUser taskUser : taskUserList) {
            // 已取消
            if (TaskUtil.executeStatusYqx(taskExecuteStatus)) {
                // 只要有一条不是  不派发和已反馈，则当前用户就定义为未反馈
                if (!TaskUserCancelAssignStatusEnum.BPF.getCode().equals(taskUser.getCancelAssignStatus()) && !TaskUserCancelAssignStatusEnum.YFK.getCode().equals(taskUser.getCancelAssignStatus())) {
                    boolAllFeedback = false;
                }
                continue;
            }
            // 只要有一条不是  不派发和已反馈，则当前用户就定义为未反馈
            if (!TaskUserAssignStatusEnum.BPF.getCode().equals(taskUser.getAssignStatus()) && !TaskUserAssignStatusEnum.YFK.getCode().equals(taskUser.getAssignStatus())) {
                boolAllFeedback = false;
            }
        }
        // 如果是全部反馈完成
        if (boolAllFeedback) {
            if (!TaskUtil.executeStatusYqx(taskExecuteStatus)) {
                // 已反馈
                taskExecuteStatus = TaskExecuteStatusEnum.YFK.getCode();
            }
            // 执行状态ID
            task.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(task.getId(), taskExecuteStatus));
        }
        // 修改任务表
        task.setTaskUserList(taskUserList);
        TaskUtil.calQty(task);
        taskService.updateById(task);
    }

    @Override
    public void recoverCancel(Serializable id) {
        TaskUser taskUser = this.selectExtensionById(id);
        // 停电任务
        Task task = taskUser.getTask();
        // 判断该通知下有多少停电用户
        List<TaskUser> taskUserList = baseMapper.searchExtensionPageList(new TaskUserQueryCommand().setTaskId(task.getId()));
        String updateExecuteStatus = TaskExecuteStatusEnum.DPF.getCode();
        // 如果只有一个用户，变更通知单状态为 待派发
        if (taskUserList.size() > 1) {
            // 如果存在多个用户，需要判断是否都是未派发或者不派发，如果都是未派发或者不派发，转变为 待派发，反之 转变为 执行中
            boolean exist = taskUserList.stream().filter(t -> !t.getId().equals(Long.parseLong(id.toString())))
                    .anyMatch(t -> t.getAssignStatus().equals(TaskUserAssignStatusEnum.YPF.getCode())
                            || t.getAssignStatus().equals(TaskUserAssignStatusEnum.YFK.getCode()));
            // 如果存在 已派发 或 已反馈，转换为 执行中
            if (exist) {
                updateExecuteStatus = TaskExecuteStatusEnum.ZXZ.getCode();
            } else {
                updateExecuteStatus = TaskExecuteStatusEnum.DPF.getCode();
            }
        }
        // 执行状态
        String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(task);
        // 已取消
        if (TaskUtil.executeStatusYqx(taskExecuteStatus)) {
            if (!TaskUserCancelAssignStatusEnum.BPF.getCode().equals(taskUser.getCancelAssignStatus())) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务通知用户[%s]必须是不派发才能进行恢复派发操作", taskUser.getReceiptCode()));
            }
            // 恢复成未派发
            this.lambdaUpdate().set(TaskUser::getCancelAssignStatus, TaskUserCancelAssignStatusEnum.WPF.getCode())
                    .set(TaskUser::getCancelReason, null)
                    .set(TaskUser::getCancelAssignMethod, null)
                    .eq(TaskUser::getId, taskUser.getId()).update();
            // 变更通知单状态
            task.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(task.getId(), updateExecuteStatus));
            taskService.updateById(task);
        } else {
            if (!TaskUserAssignStatusEnum.BPF.getCode().equals(taskUser.getAssignStatus())) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("停电任务通知用户[%s]必须是不派发才能进行恢复派发操作", taskUser.getReceiptCode()));
            }
            // 恢复成未派发
            this.lambdaUpdate().set(TaskUser::getAssignStatus, TaskUserAssignStatusEnum.WPF.getCode())
                    .set(TaskUser::getCancelReason, null)
                    .set(TaskUser::getAssignMethod, null)
                    .set(TaskUser::getAssignTime, null)
                    .eq(TaskUser::getId, taskUser.getId()).update();
            // 变更通知单状态
            task.setExecuteStatusId(taskExecuteStatusService.saveTaskExecuteStatus(task.getId(), updateExecuteStatus));
            taskService.updateById(task);
        }
    }

    @Override
    public void deleteByTaskId(Serializable taskId) {
        this.getBaseMapper().deleteByTaskId(taskId);
    }

    @Override
    public TaskUserTotalResource getTaskUserTotal(TaskUserTotalQueryCommand command) {
        // 当前用户所属工程队
        command.setEngineersTeamId(TaskUtil.getEngineersTeamId());
        List<String> executeStatusList = List.of(TaskExecuteStatusEnum.ZXZ.getCode(), TaskExecuteStatusEnum.DPF.getCode());
        command.setExecuteStatusList(executeStatusList);
        // 统计信息对象
        TaskUserTotalResource totalResource = new TaskUserTotalResource();
        // 未派发数量
        command.setAssignStatus(TaskUserAssignStatusEnum.WPF.getCode());
        totalResource.setUndistributedQty(this.baseMapper.getTaskUserTotal(command));
        // 未签数量
        command.setAssignStatus(TaskUserAssignStatusEnum.YPF.getCode());
        command.setFeedbackStatus(TaskUserFeedbackStatusEnum.WQ.getCode());
        totalResource.setNoFeedbackQty(this.baseMapper.getTaskUserTotal(command));
        // 已拒签数量
        command.setAssignStatus(TaskUserAssignStatusEnum.YFK.getCode());
        command.setFeedbackStatus(TaskUserFeedbackStatusEnum.JQ.getCode());
        totalResource.setRefusedSignQty(this.baseMapper.getTaskUserTotal(command));
        // 已超时数量
        command.setAssignStatus(TaskUserAssignStatusEnum.YPF.getCode());
        command.setFeedbackStatus(TaskUserFeedbackStatusEnum.WQ.getCode());
        command.setSqlType("timeOut");
        totalResource.setTimeOutQty(this.baseMapper.getTaskUserTotal(command));
        // 取消通知统计, 清空条件
        command.setAssignStatus(null);
        command.setFeedbackStatus(null);
        command.setSqlType(null);
        // 取消通知未派发数量
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YQX.getCode()));
        command.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.WPF.getCode());
        totalResource.setCancelUndistributedQty(this.baseMapper.getTaskUserTotal(command));
        // 取消通知未签数量
        command.setCancelAssignStatus(TaskUserCancelAssignStatusEnum.YPF.getCode());
        command.setCancelFeedbackStatus(TaskUserCancelFeedbackStatusEnum.WQ.getCode());
        totalResource.setCancelNoFeedbackQty(this.baseMapper.getTaskUserTotal(command));
        return totalResource;
    }

    @Override
    public TaskUserSumTotalResource getTaskUserSumTotal(TaskUserTotalQueryCommand command) {
        // 只统计已完成的数据
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.YWC.getCode()));
        // 统计信息对象
        TaskUserSumTotalResource totalSumResource = new TaskUserSumTotalResource();
        // 停电用户总数量
        totalSumResource.setTaskUserSumQty(this.baseMapper.getTaskUserTotal(command));
        // 停电区域数量
        totalSumResource.setPowerCutAreaQty(this.baseMapper.getTaskUserAreaTotal(command));
        // 重复停电区域数量
        command.setBoolRepeatPowerCut(YesOrNoEnum.YES.getCode());
        totalSumResource.setPowerCutRepeatAreaQty(this.baseMapper.getTaskUserAreaTotal(command));
        // 查询前一年重复停电区域数量
        command.setCreateDateBegin(command.getCreateDateBegin().plusYears(-1));
        command.setCreateDateEnd(command.getCreateDateEnd().plusYears(-1));
        totalSumResource.calPowerCutRepeatAreaTb(this.baseMapper.getTaskUserAreaTotal(command));
        // 查询前一年重复停电区域数量
        command.setBoolRepeatPowerCut(null);
        totalSumResource.calPowerCutAreaTb(this.baseMapper.getTaskUserAreaTotal(command));
        // 查询前一年停电区域数量
        totalSumResource.calPowerCutAreaTb(this.baseMapper.getTaskUserAreaTotal(command));
        // 查询前一年停电用户数量
        totalSumResource.calTaskUserTb(this.baseMapper.getTaskUserTotal(command));
        return totalSumResource;
    }

    @Override
    public InputStream genTaskQrCode(TaskUser taskUser) {
        // Logo图片
        ClassPathResource classPathResource = new ClassPathResource("/static/logo.jpg");
        InputStream inputStream1 = classPathResource.getStream();
        // 生成二维码
        return ZXingCode.getLogoQrCode(taskUser.getId().toString(), inputStream1, "");
    }

    /**
     * 统计用户对象为 非抄送用户
     */
    @Override
    public Map<String, Integer> repeatPowerCutAdvance(List<TaskUser> sourceTaskUserList, List<TaskUser> taskUserDbList, Task task, boolean boolSaveAdvance) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        // 过滤掉 抄送用户
        sourceTaskUserList = sourceTaskUserList.stream().filter(e -> e.getUserType() != null && e.getUserType() != 7).collect(Collectors.toList());
        taskUserDbList = taskUserDbList.stream().filter(e -> e.getUserType() != null && e.getUserType() != 7).collect(Collectors.toList());

        try {
            if (CollectionUtils.isEmpty(sourceTaskUserList)) {
                throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, "停电用户数据不能为空，请检查");
            }
            // 如果数据库没有，则不需要判断是否重复预警了
            if (CollectionUtils.isEmpty(taskUserDbList)) {
                return resultMap;
            }
            // 获取重复停电预警规则
            Map<String, Integer> voltageLevelMap = advanceRuleFeignClient.getRepeatPowerCutRule().getData();
            if (ObjectUtils.isEmpty(voltageLevelMap)) {
                throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "未查询到重复停电预警规则数据，请检查");
            }
            // 最近一次停电时间Map
            Map<String, String> maxTimeMap = Maps.newHashMap();
            for (TaskUser taskUser : taskUserDbList) {
                // 户号+电压等级作为Key
                String key = taskUser.getAccountNumber() + "_" + taskUser.getVoltageLevel();
                maxTimeMap.put(key, taskUser.getAttribute1());
            }
            List<AdvanceParamCommand> addAdvanceList = Lists.newArrayList();
            List<TaskUser> updateTaskUserList = Lists.newArrayList();
            // 循环需要告警的数据
            for (TaskUser entity : sourceTaskUserList) {
                // 电压等级对应的天数
                Integer days = voltageLevelMap.get(entity.getVoltageLevel());
                if (days == null) {
                    throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("电压等级[%s]预警规则未维护，请检查", entity.getVoltageLevel()));
                }
                // 户号+电压等级作为Key
                String key = entity.getAccountNumber() + "_" + entity.getVoltageLevel();
                // 最近一次停电时间
                String lastPowerOutageTime = maxTimeMap.get(key);
                if (StringUtils.isBlank(lastPowerOutageTime)) {
                    continue;
                }
                // 上次停电时间
                LocalDateTime lastPowerOutageTimeLocal = LocalDateTime.parse(lastPowerOutageTime, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 计算上次停电时间跟本次停电时间的天数
                int daysNum = (int) (task.getStartTime().toLocalDate().toEpochDay() - lastPowerOutageTimeLocal.toLocalDate().toEpochDay());
                // 如果天数大于阈值  不需要预警
                if (daysNum > days) {
                    continue;
                }
                // 当前用户是重复停电
                entity.setBoolRepeatPowerCut(YesOrNoEnum.YES.getCode());
                // 上次停电时间
                entity.setLastPowerCutTime(lastPowerOutageTimeLocal);
                // 需要添加告警
                if (boolSaveAdvance) {
                    addAdvanceList.add(AdvanceParamCommand.builder().businessId(entity.getId()).businessCode(entity.getReceiptCode())
                            .accountNumber(entity.getAccountNumber()).taskCode(task.getTaskCode()).startTime(task.getStartTime())
                            .endTime(task.getEndTime()).lastTime(lastPowerOutageTimeLocal).days(days).build());
                    updateTaskUserList.add(entity);
                }
            }
            // 保存预警信息
            if (boolSaveAdvance) {
                resultMap = advanceNoticeFeignClient.saveRepeatPowerCutAdvance(AdvanceNoticeSaveCommand.builder()
                        .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode()).businessList(addAdvanceList).build()).getData();
                // 更新用户告警状态
                this.updateBatchById(updateTaskUserList);
            }
        } catch (Exception e) {
            log.error("======= 重复停电预警保存失败  ======", e);
        }
        return resultMap;
    }

    @Override
    public void repeatPowerCutCancelAdvance(List<Serializable> taskUserIdList) {
        ThreadUtil.execAsync(() -> {
            try {
                advanceNoticeFeignClient.cancelRepeatPowerCutAdvanceStatus(taskUserIdList);
            } catch (Exception e) {
                log.error(String.format("取消用户[%s]重复停电告警失败", taskUserIdList.toString()), e);
            }
        });
    }

    @Override
    public Map<String, Integer> userRejectAdvance(TaskUser entity) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            // 获取反馈超期告警规则天数
            TaskAdvanceRuleResource ruleResource = advanceRuleFeignClient.getFeedbackTimeOutRule().getData();
            if (ObjectUtils.isEmpty(ruleResource)) {
                throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "未查询到反馈超期告警规则数据，请检查");
            }
            // 停电通知
            Task task = entity.getTask();
            // 用户最近的一次拒签时间
            Map<String, String> maxFeedbackTimeMap = this.selectMaxFeedbackTime(Collections.singletonList(entity.getId()));
            String feedbackTime = maxFeedbackTimeMap.get(entity.getId().toString());
            LocalDateTime feedbackTimeLocal = LocalDateTime.now();
            if (StringUtils.isNotBlank(feedbackTime)) {
                feedbackTimeLocal = LocalDateTime.parse(feedbackTime, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
            }
            // 用户拒签告警
            List<AdvanceParamCommand> addAdvanceList = Lists.newArrayList();
            addAdvanceList.add(AdvanceParamCommand.builder().businessId(entity.getId()).businessCode(entity.getReceiptCode())
                    .accountNumber(entity.getAccountNumber()).taskCode(task.getTaskCode()).startTime(task.getStartTime()).endTime(task.getEndTime())
                    .lastTime(feedbackTimeLocal).days(ruleResource.getDays()).build());
            // 保存告警数据
            resultMap = advanceNoticeFeignClient.saveUserRejectAdvance(AdvanceNoticeSaveCommand.builder()
                    .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode()).businessList(addAdvanceList)
                    .days(ruleResource.getDays()).build()).getData();
        } catch (Exception e) {
            log.error("======= 用户拒签预警保存失败  ======", e);
        }
        return resultMap;
    }

    @Override
    public void userRejectCancelAdvance(List<Serializable> taskUserIdList) {
        ThreadUtil.execAsync(() -> {
            try {
                advanceNoticeFeignClient.cancelUserRejectAdvanceStatus(taskUserIdList);
            } catch (Exception e) {
                log.error(String.format("取消用户[%s]用户拒签告警失败", taskUserIdList.toString()), e);
            }
        });
    }

    @Override
    public Map<String, Integer> releaseTimeOutAdvance(List<TaskUser> taskUserList) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            // 获取下达超时预警规则
            TaskAdvanceRuleResource ruleResource = advanceRuleFeignClient.getReleaseTimeOutRule().getData();
            if (ObjectUtils.isEmpty(ruleResource)) {
                log.error("======= 未查询到下达超时预警规则数据，请检查  ======");
                return resultMap;
            }
            List<AdvanceParamCommand> addAdvanceList = Lists.newArrayList();
            // 循环需要告警的数据
            for (TaskUser taskUser : taskUserList) {
                // 停电任务
                Task task = taskUser.getTask();
                // 执行状态
                String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(task);
                // 必须是执行中才会有可能有未签数据
                if (!TaskUtil.executeStatusZxz(taskExecuteStatus)) {
                    continue;
                }
                // 派发状态
                String assignStatus = taskUser.getAssignStatus();
                if (!TaskUserAssignStatusEnum.YPF.getCode().equals(assignStatus)) {
                    continue;
                }
                // 反馈状态
                String feedbackStatus = taskUser.getFeedbackStatus();
                if (!TaskUserFeedbackStatusEnum.WQ.getCode().equals(feedbackStatus)) {
                    continue;
                }
                // 计算停电日期与系统日期的时长小于等于*天
                int daysNum = (int) (task.getStartTime().toLocalDate().toEpochDay() - LocalDateTime.now().toLocalDate().toEpochDay());
                // 如果天数大于阈值  不需要预警
                if (daysNum > ruleResource.getDays()) {
                    continue;
                }
                // 需要添加告警
                if (!ObjectUtils.isEmpty(taskUser.getId())) {
                    addAdvanceList.add(AdvanceParamCommand.builder().businessId(taskUser.getId()).businessCode(taskUser.getReceiptCode())
                            .accountNumber(taskUser.getAccountNumber()).taskCode(task.getTaskCode()).startTime(task.getStartTime())
                            .endTime(task.getEndTime()).days(ruleResource.getDays()).build());
                }
            }
            // 保存下达超时预警
            if (!CollectionUtils.isEmpty(addAdvanceList)) {
                resultMap = advanceNoticeFeignClient.saveReleaseTimeOutAdvance(AdvanceNoticeSaveCommand.builder()
                        .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode()).businessList(addAdvanceList)
                        .days(ruleResource.getDays()).build()).getData();
            }
        } catch (Exception e) {
            log.error("=======  下达超时预警保存失败  ======", e);
        }
        return resultMap;
    }

    @Override
    public void releaseTimeOutCancelAdvance(List<Serializable> taskUserIdList) {
        ThreadUtil.execAsync(() -> {
            try {
                advanceNoticeFeignClient.cancelReleaseTimeOutAdvanceStatus(taskUserIdList);
            } catch (Exception e) {
                log.error(String.format("取消用户[%s]下达超时告警失败", taskUserIdList.toString()), e);
            }
        });
    }

    @Override
    public EngineersSumResource engineersSummary() {
        Serializable engineersTeamId = TaskUtil.getEngineersTeamId();
        // 获取当前时间和本年1月1日0点以及去年的时间节点以用于计算增加和环比
        LocalDateTime thisYearDateTimeEnd = LocalDateTime.now();
        LocalDateTime thisYearDateTimeBegin = LocalDateTime.of(thisYearDateTimeEnd.getYear(), 1, 1, 0, 0, 0, 0);
        LocalDateTime lastYearDateTimeEnd = thisYearDateTimeEnd.minusYears(1);
        LocalDateTime lastYearDateTimeBegin = thisYearDateTimeBegin.minusYears(1);

        // 查询去年开始日期到今年结束日期的所有数据
        EngineerSummaryQueryCommand queryCommand = EngineerSummaryQueryCommand.builder()
                .engineersTeamId(engineersTeamId).startDateTime(lastYearDateTimeBegin).endDateTime(thisYearDateTimeEnd).build();

        List<TaskUser> allYearTaskUserList = this.baseMapper.listByExecuteStatusAndDate(queryCommand);
        List<TaskUser> thisYearTaskUserList = new ArrayList<>();
        List<TaskUser> lastYearTaskUserList = new ArrayList<>();
        for (TaskUser taskUser : allYearTaskUserList) {
            if (!taskUser.getStartTime().isAfter(lastYearDateTimeEnd)) {
                lastYearTaskUserList.add(taskUser);
            } else if (!taskUser.getStartTime().isBefore(thisYearDateTimeBegin)) {
                thisYearTaskUserList.add(taskUser);
            }
        }

        // 数据汇总
        return this.summaryByEngineers(thisYearTaskUserList, lastYearTaskUserList);
    }

    @Override
    public Map<String, Object> importTaskUser(List<List<Object>> list, String startTime) {
        if (StringUtils.isBlank(startTime)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "因导入会做用户重复停电校验，请选择停电时间再进行导入");
        }
        List<TaskUser> taskUserList = Lists.newArrayList();
        // 用户重要性字典Map
        Map<String, String> userPriorityMap = TaskUtil.getLovLileNameMap(sysLovService.searchListByCode(TaskConstants.USER_PRIORITY_LOV_KEY));
        // 用户类型字典Map
        Map<String, String> userTypeMap = TaskUtil.getLovLileNameMap(sysLovService.searchListByCode(TaskConstants.USER_TYPE_LOV_KEY));
        // 错误信息
        List<String> errMsgList = Lists.newArrayList();
        // 户号跟下标Map
        Map<String, Integer> accountNumberMap = Maps.newHashMap();
        // 循环数据
        int idx = 2;
        for (List<Object> dataList : list) {
            // 是否结束循环
            boolean isContinue = false;
            // 用户类型
            String userType = TaskUtil.convertToString(dataList, 0);
            if (StringUtils.isNotBlank(userType)) {
                if (StringUtils.isBlank(userTypeMap.get(userType))) {
                    errMsgList.add(String.format("第[%s]行用户类型[%s]数据填写不正确，可填写内容%s", idx, userType, userTypeMap.keySet()));
                    isContinue = true;
                }
            }
            // 户号
            String accountNumber = TaskUtil.convertToString(dataList, 1);
            // 如果说包含Key，说明当前Excel就有重复的户号
            if (accountNumberMap.containsKey(accountNumber)) {
                errMsgList.add(String.format("第[%s]行跟第[%s]行户号[%s]重复，请检查", idx, accountNumberMap.get(accountNumber), accountNumber));
                isContinue = true;
            } else {
                accountNumberMap.put(accountNumber, idx);
            }
            // 用户重要性
            String userPriority = TaskUtil.convertToString(dataList, 4);
            if (StringUtils.isNotBlank(userPriority)) {
                if (StringUtils.isBlank(userPriorityMap.get(userPriority))) {
                    errMsgList.add(String.format("第[%s]行用户重要性[%s]数据填写不正确，可填写内容%s", idx, userPriority, userPriorityMap.keySet()));
                    isContinue = true;
                }
            }
            idx++;
            // 只要本次循环有错误信息，则不必要往下面执行
            if (isContinue) {
                continue;
            }
            TaskUser taskUser = new TaskUser();
            taskUser.setUserType(TaskUtil.convertToInteger(userTypeMap.get(userType)));
            taskUser.setAccountNumber(accountNumber);
            taskUser.setCustomerName(TaskUtil.convertToString(dataList, 2));
            // 是否短时停电
            String boolShortTime = TaskUtil.convertToString(dataList, 3);
            if ("是".equals(boolShortTime)) {
                boolShortTime = "1";
            } else if ("否".equals(boolShortTime)) {
                boolShortTime = "0";
            }
            taskUser.setBoolShortTime(TaskUtil.convertToInteger(boolShortTime));
            taskUser.setUserPriority(TaskUtil.convertToInteger(userPriorityMap.get(userPriority)));
            taskUser.setRegion(TaskUtil.convertToString(dataList, 5));
            taskUser.setElectricalNumber(TaskUtil.convertToString(dataList, 6));
            taskUser.setVoltageLevel(TaskUtil.convertToString(dataList, 7));
            taskUser.setAddress(TaskUtil.convertToString(dataList, 8));
            taskUser.setPostCode(TaskUtil.convertToString(dataList, 9));
            taskUser.setPhone(TaskUtil.convertToString(dataList, 10));
            taskUser.setCustomerAddress(TaskUtil.convertToString(dataList, 11));
            taskUser.setStationLineName(TaskUtil.convertToString(dataList, 12));
            taskUser.setAccessPoint(TaskUtil.convertToString(dataList, 13));
            taskUser.setAccessPointName(TaskUtil.convertToString(dataList, 14));
            taskUser.setCapacity(TaskUtil.convertToString(dataList, 15));
            taskUser.setRemark(TaskUtil.convertToString(dataList, 16));
            // 判断用户是否重复停电
            taskUserList.add(taskUser);
        }
        // 获取所有户号对应的备用联系方式
        Map<String, String> backupPhoneMap = taskUserBackupPhoneService.searchListByAccountNumber(taskUserList.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList()));
        taskUserList.forEach(e -> e.setBackupPhone(backupPhoneMap.get(e.getAccountNumber())));
        // 数据返回
        Map<String, Object> resultMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(errMsgList)) {
            resultMap.put("code", 0);
            resultMap.put("data", taskUserList);
        } else {
            resultMap.put("code", SystemError.PARAM_NOT_CHECKED_ERROR.getCode());
            resultMap.put("msg", errMsgList);
        }
        return resultMap;
    }

    @Override
    public List<TaskUser> searchTaskUserList(List<String> accountNumberList) {
        return this.baseMapper.searchTaskUserList(accountNumberList);
    }

    /**
     * 工程队数据汇总
     *
     * @param thisYearList 今年停电用户集合
     * @param lastYearList 去年停电用户集合
     * @return {@link EngineersSumResource}
     */
    private EngineersSumResource summaryByEngineers(List<TaskUser> thisYearList, List<TaskUser> lastYearList) {
        EngineersSumResource thisYearResource = this.engineersQty(thisYearList);
        EngineersSumResource lastYearResource = this.engineersQty(lastYearList);
        // 计算停电通知派发-同比增加
        thisYearResource.setAssignIncrease(thisYearResource.getAssignQty() - lastYearResource.getAssignQty());
        // 计算停电通知派发-同比上涨百分比，如果去年同期是0，则记为增加100%
        thisYearResource.setAssignRisePct(BigDecimal.valueOf(100.00));
        if (lastYearResource.getAssignQty() != 0) {
            thisYearResource.setAssignRisePct(BigDecimal.valueOf(thisYearResource.getAssignIncrease())
                            .divide(BigDecimal.valueOf(lastYearResource.getAssignQty()), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
        }
        // 计算取消停电派发-同比增加
        thisYearResource.setCancelAssignIncrease(thisYearResource.getCancelAssignQty() - lastYearResource.getCancelAssignQty());
        // 计算取消停电派发-同比上涨，如果去年同期是0，则记为增加100%
        thisYearResource.setCancelAssignRisePct(BigDecimal.valueOf(100.00));
        if (lastYearResource.getCancelAssignQty() != 0) {
            thisYearResource.setCancelAssignRisePct(BigDecimal.valueOf(thisYearResource.getCancelAssignIncrease())
                            .divide(BigDecimal.valueOf(lastYearResource.getCancelAssignQty()), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
        }
        // 计算派发超时-同比增加
        thisYearResource.setOverTimeIncrease(thisYearResource.getOverTimeQty() - lastYearResource.getOverTimeQty());
        // 计算派发超时-同比上涨，如果去年同期是0，则记为增加100%
        thisYearResource.setOverTimeRisePct(BigDecimal.valueOf(100.00));
        if (lastYearResource.getOverTimeQty() != 0) {
            thisYearResource.setOverTimeRisePct(BigDecimal.valueOf(thisYearResource.getOverTimeIncrease())
                            .divide(BigDecimal.valueOf(lastYearResource.getOverTimeQty()), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
        }
        return thisYearResource;
    }

    private EngineersSumResource engineersQty(List<TaskUser> taskUserList) {
        EngineersSumResource resource = EngineersSumResource.builder().build();
        taskUserList.forEach(t -> {
            // 任务执行状态
            String taskExecuteStatus = t.getTaskExecuteStatus();
            // 任务通知用户派发状态
            String assignStatus = t.getAssignStatus();
            // 任务通知用户反馈状态
            String feedbackStatus = t.getFeedbackStatus();
            // 获取下达超期预警警规则天数
            TaskAdvanceRuleResource ruleResource = advanceRuleFeignClient.getReleaseTimeOutRule().getData();
            if (ObjectUtils.isEmpty(ruleResource)) {
                throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "未查询到下达超期预警规则数据，请检查");
            }
            // 统计派发数量，需要确保父级停电任务是执行中+已完成+已反馈
            if ((TaskExecuteStatusEnum.ZXZ.getCode().equals(taskExecuteStatus)
                    || TaskExecuteStatusEnum.YWC.getCode().equals(taskExecuteStatus)
                    || TaskExecuteStatusEnum.YFK.getCode().equals(taskExecuteStatus))
                    && TaskUserAssignStatusEnum.YFK.getCode().equals(assignStatus)) {
                resource.setAssignQty(resource.getAssignQty() + 1);
            }
            // 统计取消派发数量，需要确保父级停电任务是已取消
            if (TaskExecuteStatusEnum.YQX.getCode().equals(taskExecuteStatus) && TaskUserCancelAssignStatusEnum.YFK.getCode().equals(t.getCancelAssignStatus())) {
                resource.setCancelAssignQty(resource.getCancelAssignQty() + 1);
            }
            // 统计派发超时数量 需满足以下条件：1-通知已派发给用户；2-用户必须是未签；3-停电任务状态是已完成；4-停电时间减第一次反馈提交的时间小于阈值
            if (TaskUserAssignStatusEnum.YPF.getCode().equals(assignStatus) && TaskUserFeedbackStatusEnum.WQ.getCode().equals(feedbackStatus)
                    && TaskExecuteStatusEnum.YWC.getCode().equals(taskExecuteStatus)
                    && (t.getStartTime() != null && t.getFeedbackTime() != null && Duration.between(t.getStartTime(), t.getFeedbackTime()).toDays() < ruleResource.getDays())) {
                resource.setOverTimeQty(resource.getOverTimeQty() + 1);
            }
        });
        return resource;
    }

}