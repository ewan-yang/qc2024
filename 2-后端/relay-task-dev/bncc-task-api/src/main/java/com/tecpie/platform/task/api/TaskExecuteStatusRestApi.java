package com.tecpie.platform.task.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusPageQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusQueryCommand;
import com.tecpie.platform.task.api.resource.TaskExecuteStatusResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务执行状态表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务执行状态表接口定义")
@Headers("Accept: application/json")
public interface TaskExecuteStatusRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<TaskExecuteStatusResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/taskExecuteStatus/{id}")
    Result<TaskExecuteStatusResource> getTaskExecuteStatus(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < TaskExecuteStatusResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/taskExecuteStatus/list")
    Result<List<TaskExecuteStatusResource>> searchTaskExecuteStatusList(TaskExecuteStatusQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < TaskExecuteStatusResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/taskExecuteStatus/page")
    Result<PagedResult<TaskExecuteStatusResource>> searchTaskExecuteStatusPage(TaskExecuteStatusPageQueryCommand command);

}