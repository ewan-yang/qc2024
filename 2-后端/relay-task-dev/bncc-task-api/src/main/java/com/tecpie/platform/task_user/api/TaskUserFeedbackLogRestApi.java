package com.tecpie.platform.task_user.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFeedbackSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserFeedbackLogResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务用户反馈表接口定义")
@Headers("Accept: application/json")
public interface TaskUserFeedbackLogRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<TaskUserFeedbackLogResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/taskUserFeedbackLog/{id}")
    Result<TaskUserFeedbackLogResource> getTaskUserFeedbackLog(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < TaskUserFeedbackLogResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/taskUserFeedbackLog/list")
    Result<List<TaskUserFeedbackLogResource>> searchTaskUserFeedbackLogList(TaskUserFeedbackLogQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < TaskUserFeedbackLogResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/taskUserFeedbackLog/page")
    Result<PagedResult<TaskUserFeedbackLogResource>> searchTaskUserFeedbackLogPage(TaskUserFeedbackLogPageQueryCommand command);

    /**
     * 根据停电用户ID获取反馈记录
     *
     * @param taskUserId 任务用户ID
     * @return Result<TaskUserFeedbackLogResource>
     */
    @Operation(summary = "根据停电用户ID获取反馈记录")
    @RequestLine("GET /api/v1/taskUserFeedbackLog/list/{taskUserId}")
    Result<List<TaskUserFeedbackLogResource>> searchListByTaskUserId(@Param("taskUserId") Serializable taskUserId);

    /**
     * 保存停电任务用户反馈表数据
     *
     * @param command
     * @return Result<Long>
     */
    @Operation(summary = "保存停电任务用户反馈表数据")
    @RequestLine("POST /api/v1/taskUserFeedbackLog")
    Result<Long> saveTaskUserFeedback(TaskUserFeedbackSaveCommand command);

}