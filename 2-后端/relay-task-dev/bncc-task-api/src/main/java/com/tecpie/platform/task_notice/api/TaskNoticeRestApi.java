package com.tecpie.platform.task_notice.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticePageQueryCommand;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticeQueryCommand;
import com.tecpie.platform.task_notice.api.command.save.TaskNoticeSaveCommand;
import com.tecpie.platform.task_notice.api.command.update.TaskNoticeUpdateCommand;
import com.tecpie.platform.task_notice.api.resource.TaskNoticeResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务通知公告表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务通知公告表接口定义")
@Headers("Accept: application/json")
public interface TaskNoticeRestApi {

  /**
   * 根据ID查询详情信息
   *
   * @param id
   * @return Result<TaskNoticeResource>
   */
  @Operation(summary = "查询详情信息")
  @RequestLine("GET /api/v1/taskNotice/{id}")
  Result<TaskNoticeResource> getTaskNotice(@Param("id") Serializable id);

  /**
   * 根据筛选条件检索列表数据
   *
   * @param command
   * @return Result<List<TaskNoticeResource>>
   */
  @Operation(summary = "检索列表数据")
  @RequestLine("POST /api/v1/taskNotice/list")
  Result<List<TaskNoticeResource>> searchTaskNoticeList(TaskNoticeQueryCommand command);

  /**
   * 根据筛选条件检索分页列表数据
   *
   * @param command
   * @return Result<PagedResult<TaskNoticeResource>>
   */
  @Operation(summary = "检索分页列表数据")
  @RequestLine("POST /api/v1/taskNotice/page")
  Result<PagedResult<TaskNoticeResource>> searchTaskNoticePage(TaskNoticePageQueryCommand command);

    /**
     * 查询当前用户所属的通知
     *
     * @param command
     * @return Result<PagedResult < TaskNoticeResource>>
     */
    @Operation(summary = "查询当前用户所属的通知")
    @RequestLine("POST /api/v1/taskNotice/currUser/page")
    Result<PagedResult<TaskNoticeResource>> searchCurrUserTaskNoticePage(TaskNoticePageQueryCommand command);

  /**
   * 保存停电任务通知公告表数据
   *
   * @param command
   * @return Result<Long>
   */
  @Operation(summary = "保存停电任务通知公告表数据")
  @RequestLine("POST /api/v1/taskNotice")
  Result<Long> saveTaskNotice(TaskNoticeSaveCommand command);

  /**
   * 根据ID更新停电任务通知公告表数据
   *
   * @param command
   * @return Result
   */
  @Operation(summary = "更新停电任务通知公告表数据")
  @RequestLine("PUT /api/v1/taskNotice/{id}")
  Result updateTaskNoticeById(@Param("id") Serializable id, TaskNoticeUpdateCommand command);

  /**
   * 根据ID删除停电任务通知公告表数据
   *
   * @param id
   * @return Result
   */
  @Operation(summary = "删除停电任务通知公告表数据")
  @RequestLine("DELETE /api/v1/taskNotice/{id}")
  Result deleteTaskNoticeById(@Param("id") Serializable id);

    /**
     * 通知读取
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "通知读取")
    @RequestLine("GET /api/v1/taskNotice/read/{id}")
    Result read(@Param("id") Serializable id);

  /**
   * 根据ID变更停电任务通知公告表状态
   *
   * @param id
   * @param status
   * @return String
   */
  @Operation(summary = "变更停电任务通知公告表状态")
  @RequestLine("PUT /api/v1/taskNotice/{id}/status/{status}")
  Result changeTaskNoticeStatusById(@Param("id") Serializable id, @Param("status") Integer status);

}