package com.tecpie.platform.task_user.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFollowSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserFollowResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户跟进情况表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务用户跟进情况表接口定义")
@Headers("Accept: application/json")
public interface TaskUserFollowRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<TaskUserFollowResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/taskUserFollow/{id}")
    Result<TaskUserFollowResource> getTaskUserFollow(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < TaskUserFollowResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/taskUserFollow/list")
    Result<List<TaskUserFollowResource>> searchTaskUserFollowList(TaskUserFollowQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < TaskUserFollowResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/taskUserFollow/page")
    Result<PagedResult<TaskUserFollowResource>> searchTaskUserFollowPage(TaskUserFollowPageQueryCommand command);

    /**
     * 根据停电用户ID获取跟进记录
     *
     * @param taskUserId 任务用户ID
     * @return Result<TaskUserFollowResource>
     */
    @Operation(summary = "根据停电用户ID获取跟进记录")
    @RequestLine("GET /api/v1/taskUserFollow/list/{taskUserId}")
    Result<List<TaskUserFollowResource>> searchListByTaskUserId(@Param("taskUserId") Serializable taskUserId);

    /**
     * 停电任务通知用户跟进
     *
     * @param command
     * @return String
     */
    @Operation(summary = "停电任务通知用户跟进")
    @RequestLine("PUT /api/v1/taskUserFollow/follow")
    Result follow(TaskUserFollowSaveCommand command);

}