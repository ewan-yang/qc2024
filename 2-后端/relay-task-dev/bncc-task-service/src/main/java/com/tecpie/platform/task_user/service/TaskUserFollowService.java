package com.tecpie.platform.task_user.service;

import com.tecpie.platform.task_user.api.command.query.TaskUserFollowQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUserFollow;
import com.tecpie.platform.task_user.mapper.TaskUserFollowMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户跟进情况表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskUserFollowService extends IBaseService<TaskUserFollowMapper, TaskUserFollow> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return TaskUserFollow
     */
    TaskUserFollow selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<TaskUserFollow>
     */
    List<TaskUserFollow> searchExtensionPageList(TaskUserFollowQueryCommand command);

    /**
     * 根据停电用户ID获取跟进记录
     *
     * @param taskUserId 任务用户ID
     * @return List<TaskUserFollow>
     */
    List<TaskUserFollow> searchListByTaskUserId(Serializable taskUserId);

    /**
     * 停电任务通知用户跟进
     *
     * @param entity
     * @return String
     */
    Serializable follow(TaskUserFollow entity);

}