package com.tecpie.platform.task_notice.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.enums.TaskNoticeReadStatusEnum;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticeQueryCommand;
import com.tecpie.platform.task_notice.entity.TaskNotice;
import com.tecpie.platform.task_notice.mapper.TaskNoticeMapper;
import com.tecpie.platform.task_notice.service.TaskNoticeService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务通知公告表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskNoticeServiceImpl extends BaseServiceImpl<TaskNoticeMapper, TaskNotice> implements TaskNoticeService {

    @Override
    public TaskNotice selectExtensionById(Serializable id) {
        TaskNotice entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务通知公告表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<TaskNotice> searchExtensionPageList(TaskNoticeQueryCommand command) {
        // 只查询近3天的数据
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(3);
        command.setCreateDateBegin(startDateTime);
        command.setCreateDateEnd(endDateTime);
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveTaskNotice(TaskNotice entity) {
        // 默认未读
        entity.setReadStatus(TaskNoticeReadStatusEnum.NO.getCode());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updateTaskNotice(Serializable id, TaskNotice entity) {
        TaskNotice existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务通知公告表[%s]不存在", id));
        }

        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void read(Serializable id) {
        boolean result = this.lambdaUpdate()
                .set(TaskNotice::getReadStatus, TaskNoticeReadStatusEnum.YES.getCode())
                .set(TaskNotice::getReadUserId, TecpieSecurityUtil.getUser().getId())
                .set(TaskNotice::getReadTime, LocalDateTime.now())
                .eq(TaskNotice::getId, id).update();

        if (!result) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务通知公告表[%s]不存在", id));
        }
    }

    @Override
    public void changeTaskNoticeStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(TaskNotice::getStatus, status)
                .set(TaskNotice::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(TaskNotice::getUpdateDate, LocalDateTime.now())
                .eq(TaskNotice::getId, id).update();

        if (!result) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务通知公告表[%s]不存在", id));
        }
    }

    @Override
    public void pushAssignNotice(String taskCode) {
        this.pushNotices("任务派发通知", String.format("[%s]停电任务已派发，特此通知！", taskCode));
    }

    @Override
    public void pushAssignCancelNotice(String taskCode) {
        this.pushNotices("任务派发取消通知", String.format("[%s]停电任务取消通知已派发，特此通知！", taskCode));
    }

    @Override
    public void pushTaskCancelNotice(String taskCode) {
        this.pushNotices("任务取消通知", String.format("[%s]停电任务已取消，特此通知！", taskCode));
    }

    @Override
    public void pushTaskSaveNotice(String taskCode, LocalDateTime startTime, LocalDateTime endTime, String addr) {
        this.pushNotices("停电任务新增", String.format("[%s]新增停电任务，特此通知！", taskCode));
    }

    @Override
    public void pushTaskUpdateNotice(String taskCode) {
        this.pushNotices("停电任务变更", String.format("[%s]停电任务已变更，特此通知！", taskCode));
    }

    private void pushNotices(String title, String content) {
        TaskNotice taskNotice = new TaskNotice();
        taskNotice.setType(1);
        taskNotice.setTitle(title);
        taskNotice.setContent(content);
        taskNotice.setPushTime(LocalDateTime.now());
        taskNotice.setReadStatus(1);
        taskNotice.setReadTime(LocalDateTime.now());
        save(taskNotice);
    }
}