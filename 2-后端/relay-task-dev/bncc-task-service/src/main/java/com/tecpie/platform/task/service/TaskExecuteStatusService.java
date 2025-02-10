package com.tecpie.platform.task.service;

import com.tecpie.platform.task.api.command.query.TaskExecuteStatusQueryCommand;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import com.tecpie.platform.task.mapper.TaskExecuteStatusMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务执行状态表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskExecuteStatusService extends IBaseService<TaskExecuteStatusMapper, TaskExecuteStatus> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return TaskExecuteStatus
     */
    TaskExecuteStatus selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<TaskExecuteStatus>
     */
    List<TaskExecuteStatus> searchExtensionPageList(TaskExecuteStatusQueryCommand command);

    /**
     * 保存执行状态
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveTaskExecuteStatus(TaskExecuteStatus entity);

    /**
     * 保存执行状态
     *
     * @param taskId        停电任务ID
     * @param executeStatus 执行状态
     * @return Serializable
     */
    Serializable saveTaskExecuteStatus(Serializable taskId, String executeStatus);

    /**
     * 根据停电任务ID物理删除执行状态
     *
     * @param taskId 任务ID
     */
    void deleteByTaskId(Serializable taskId);
}