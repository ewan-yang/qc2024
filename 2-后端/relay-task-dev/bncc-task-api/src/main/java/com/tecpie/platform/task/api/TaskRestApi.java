package com.tecpie.platform.task.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task.api.command.query.TaskPageQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskTotalQueryCommand;
import com.tecpie.platform.task.api.command.save.TaskSaveCommand;
import com.tecpie.platform.task.api.command.update.TaskCancelCommand;
import com.tecpie.platform.task.api.command.update.TaskUpdateCommand;
import com.tecpie.platform.task.api.resource.TaskResource;
import com.tecpie.platform.task.api.resource.TaskSumTotalResource;
import com.tecpie.platform.task.api.resource.TaskTotalResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务表接口定义")
@Headers("Accept: application/json")
public interface TaskRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<TaskResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/task/{id}")
    Result<TaskResource> getTask(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < TaskResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/task/list")
    Result<List<TaskResource>> searchTaskList(TaskQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < TaskResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/task/page")
    Result<PagedResult<TaskResource>> searchTaskPage(TaskPageQueryCommand command);

    /**
     * 根据任务ID查询相关版本数据，并按照版本排序
     *
     * @param id
     * @return Result<TaskResource>
     */
    @Operation(summary = "根据任务ID查询相关版本数据，并按照版本排序")
    @RequestLine("GET /api/v1/task/searchVersionList/{id}")
    Result<List<TaskResource>> searchVersionList(@Param("id") Serializable id);

    /**
     * 保存停电任务表数据
     *
     * @param command
     * @return Result<Serializable>
     */
    @Operation(summary = "保存停电任务表数据")
    @RequestLine("POST /api/v1/task")
    Result<Serializable> saveTask(TaskSaveCommand command);

    /**
     * 根据ID更新停电任务表数据
     *
     * @param command
     * @return Result
     */
    @Operation(summary = "更新停电任务表数据")
    @RequestLine("PUT /api/v1/task/{id}")
    Result updateTaskById(@Param("id") Serializable id, TaskUpdateCommand command);

    /**
     * 根据ID删除停电任务表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除停电任务表数据")
    @RequestLine("DELETE /api/v1/task/{id}")
    Result deleteTaskById(@Param("id") Serializable id);

    /**
     * 批量删除停电任务表数据
     *
     * @param ids
     * @return Result
     */
    @Operation(summary = "批量删除停电任务表数据")
    @RequestLine("DELETE /api/v1/task/batchDelete/{ids}")
    Result deleteBatchIds(@Param("ids") String ids);

    /**
     * 批量取消停电任务
     *
     * @param ids
     * @return String
     */
    @Operation(summary = "批量取消停电任务")
    @RequestLine("PUT /api/v1/task/endTask/{ids}")
    Result endTaskByIds(@Param("ids") String ids);

    /**
     * 取消停电任务
     *
     * @param taskCancelCommand 取消通知参数
     * @return String
     */
    @Operation(summary = "取消停电任务")
    @RequestLine("PUT /api/v1/task/cancelTask")
    Result cancelTask(TaskCancelCommand taskCancelCommand);

    /**
     * 撤销 取消停电任务
     *
     * @param taskCancelCommand 撤销 取消通知参数
     * @return String
     */
    @Operation(summary = "撤销 取消停电任务")
    @RequestLine("PUT /api/v1/task/revokeCancelTask")
    Result revokeCancelTask(TaskCancelCommand taskCancelCommand);

    /**
     * 停电任务统计信息
     *
     * @param command 任务统计请求参数
     * @return Result<TaskTotalResource>
     */
    @Operation(summary = "停电任务统计信息")
    @RequestLine("POST /api/v1/task/total")
    Result<TaskTotalResource> getTaskTotal(TaskTotalQueryCommand command);

    /**
     * 停电任务数据汇总
     *
     * @param command 任务统计请求参数
     * @return Result<TaskSumTotalResource>
     */
    @Operation(summary = "停电任务数据汇总")
    @RequestLine("POST /api/v1/task/sumTotal")
    Result<TaskSumTotalResource> getTaskSumTotal(TaskTotalQueryCommand command);
}