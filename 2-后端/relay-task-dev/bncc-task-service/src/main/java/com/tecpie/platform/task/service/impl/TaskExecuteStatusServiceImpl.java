package com.tecpie.platform.task.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusQueryCommand;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import com.tecpie.platform.task.mapper.TaskExecuteStatusMapper;
import com.tecpie.platform.task.service.TaskExecuteStatusService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务执行状态表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskExecuteStatusServiceImpl extends BaseServiceImpl<TaskExecuteStatusMapper, TaskExecuteStatus> implements TaskExecuteStatusService {

    @Override
    public TaskExecuteStatus selectExtensionById(Serializable id) {
        TaskExecuteStatus entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务执行状态表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<TaskExecuteStatus> searchExtensionPageList(TaskExecuteStatusQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveTaskExecuteStatus(TaskExecuteStatus entity) {
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Serializable saveTaskExecuteStatus(Serializable taskId, String executeStatus) {
        TaskExecuteStatus taskExecuteStatus = new TaskExecuteStatus();
        taskExecuteStatus.setTaskId(taskId);
        taskExecuteStatus.setExecuteStatus(executeStatus);
        return this.saveTaskExecuteStatus(taskExecuteStatus);
    }

    @Override
    public void deleteByTaskId(Serializable taskId) {
        this.getBaseMapper().deleteByTaskId(taskId);
    }

}