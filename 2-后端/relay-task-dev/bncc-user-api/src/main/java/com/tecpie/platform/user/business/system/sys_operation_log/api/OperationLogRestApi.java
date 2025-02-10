package com.tecpie.platform.user.business.system.sys_operation_log.api;

import com.tecpie.platform.user.business.system.sys_operation_log.api.command.save.OperationLogSaveCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.update.OperationLogUpdateCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogQueryCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogPageQueryCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.resource.OperationLogResource;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志表 接口定义
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Tag(name = "操作日志表接口定义")
@Headers("Accept: application/json")
public interface OperationLogRestApi {

  /**
   * 根据ID查询详情信息
   *
   * @param id
   * @return Result<OperationLogResource>
   */
  @Operation(summary = "查询详情信息")
  @RequestLine("GET /api/v1/operationLog/{id}")
  Result<OperationLogResource> getOperationLog(@Param("id") Serializable id);

  /**
   * 根据筛选条件检索列表数据
   *
   * @param command
   * @return Result<List<OperationLogResource>>
   */
  @Operation(summary = "检索列表数据")
  @RequestLine("POST /api/v1/operationLog/list")
  Result<List<OperationLogResource>> searchOperationLogList(OperationLogQueryCommand command);

  /**
   * 根据筛选条件检索分页列表数据
   *
   * @param command
   * @return Result<PagedResult<OperationLogResource>>
   */
  @Operation(summary = "检索分页列表数据")
  @RequestLine("POST /api/v1/operationLog/page")
  Result<PagedResult<OperationLogResource>> searchOperationLogPage(OperationLogPageQueryCommand command);

  /**
   * 保存操作日志表数据
   *
   * @param command
   * @return Result<Serializable>
   */
  @Operation(summary = "保存操作日志表数据")
  @RequestLine("POST /api/v1/operationLog")
  Result<Serializable> saveOperationLog(OperationLogSaveCommand command);

  /**
   * 根据ID更新操作日志表数据
   *
   * @param command
   * @return Result
   */
  @Operation(summary = "更新操作日志表数据")
  @RequestLine("PUT /api/v1/operationLog/{id}")
  Result updateOperationLogById(@Param("id") Serializable id, OperationLogUpdateCommand command);

  /**
   * 根据ID删除操作日志表数据
   *
   * @param id
   * @return Result
   */
  @Operation(summary = "删除操作日志表数据")
  @RequestLine("DELETE /api/v1/operationLog/{id}")
  Result deleteOperationLogById(@Param("id") Serializable id);

  /**
   * 根据ID变更操作日志表状态
   *
   * @param id
   * @param status
   * @return String
   */
  @Operation(summary = "变更操作日志表状态")
  @RequestLine("PUT /api/v1/operationLog/{id}/status/{status}")
  Result changeOperationLogStatusById(@Param("id") Serializable id, @Param("status") Integer status);

}