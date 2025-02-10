package com.tecpie.platform.task_repeat.api;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task_repeat.api.resource.TaskRepeatResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * 停电任务重复统计 接口定义
 *
 * @author zhangyuheng
 * @since 2024-12-02
 */
@Tag(name = "停电任务重复统计接口定义")
@Headers("Accept: application/json")
public interface TaskRepeatRestApi {

    /**
     * 全量检索统计重复数据
     *
     * @return Result<List<TaskRepeatResource>>
     */
    @Operation(summary = "全量检索统计重复数据")
    @RequestLine("POST /api/v1/taskRepeat/all")
    Result<List<TaskRepeatResource>> searchTaskRepeatAll();

    /**
     * 单条检索统计重复数据
     *
     * @return Result<List<TaskRepeatResource>>
     */
    @Operation(summary = "单条检索统计重复数据")
    @RequestLine("POST /api/v1/taskRepeat/{taskCode}")
    Result<List<TaskRepeatResource>> searchTaskRepeatSingle(@Param("taskCode") String taskCode);

    /**
     * 全量检索更新数据状态
     *
     * @return Result
     */
    @Operation(summary = "全量或者单条检索更新数据状态")
    @RequestLine("POST /api/v1/taskRepeat/update")
    Result updateTaskRepeatAllOrSingle(TaskQueryCommand command);

}
