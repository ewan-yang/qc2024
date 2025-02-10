package com.tecpie.platform.task_user.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.enums.TaskUserAssignStatusEnum;
import com.tecpie.platform.common.enums.TaskUserFeedbackStatusEnum;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.entity.TaskUserFollow;
import com.tecpie.platform.task_user.mapper.TaskUserFollowMapper;
import com.tecpie.platform.task_user.service.TaskUserFollowService;
import com.tecpie.platform.task_user.service.TaskUserService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务用户跟进情况表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskUserFollowServiceImpl extends BaseServiceImpl<TaskUserFollowMapper, TaskUserFollow> implements TaskUserFollowService {

    private final TaskUserService taskUserService;
    private final TaskService taskService;

    public TaskUserFollowServiceImpl(TaskUserService taskUserService, TaskService taskService) {
        this.taskUserService = taskUserService;
        this.taskService = taskService;
    }

    @Override
    public TaskUserFollow selectExtensionById(Serializable id) {
        TaskUserFollow entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务用户跟进情况表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<TaskUserFollow> searchExtensionPageList(TaskUserFollowQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<TaskUserFollow> searchListByTaskUserId(Serializable taskUserId) {
        TaskUserFollowQueryCommand command = new TaskUserFollowQueryCommand();
        command.setTaskUserId(taskUserId);
        return this.searchExtensionPageList(command);
    }

    @Override
    public Serializable follow(TaskUserFollow entity) {
        // 查询任务通知用户
        TaskUser taskUser = taskUserService.getById(entity.getTaskUserId());
        if (TaskUserFeedbackStatusEnum.TY.getCode().equals(taskUser.getFeedbackStatus())) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "当前用户已经同意，不能继续进行跟进操作");
        }
        // 停电任务
        Task task = taskService.selectExtensionById(taskUser.getTaskId());
        // 获取执行状态
        String taskExecuteStatus = TaskUtil.getTaskExecuteStatus(task);
        if (TaskUtil.executeStatusValid(taskExecuteStatus)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "已取消、已完成状态的数据不允许进行用户跟进");
        }
        this.save(entity);
        taskUser.setFeedbackStatus(entity.getFeedbackStatus());
        taskUser.setFeedbackTime(LocalDateTime.now());
        taskUser.setAssignStatus(TaskUserAssignStatusEnum.YFK.getCode());
        taskUser.setAssignMethod(entity.getFollowMethod());
        // 如果是拒签
        if (entity.getFeedbackStatus().equals(TaskUserFeedbackStatusEnum.JQ.getCode())) {
            taskUser.setRejectedReason(entity.getFollowDesc());
        } else {
            // 跟进方式
            taskUser.setAssignMethod(entity.getFollowMethod());
        }
        taskUserService.updateById(taskUser);
        // 停电任务通知用户
        task.setTaskUserList(taskUserService.searchListByTaskId(task.getId()));
        TaskUtil.calQty(task);
        taskService.updateById(task);
        return entity.getId();
    }

}