package com.tecpie.platform.user.business.system.sys_operation_log.controller;

import com.tecpie.platform.user.business.system.sys_operation_log.api.OperationLogRestApi;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.save.OperationLogSaveCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.update.OperationLogUpdateCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogQueryCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogPageQueryCommand;
import com.tecpie.platform.user.business.system.sys_operation_log.api.resource.OperationLogResource;
import com.tecpie.platform.user.business.system.sys_operation_log.controller.assembler.command.save.OperationLogSaveCommandAssembler;
import com.tecpie.platform.user.business.system.sys_operation_log.controller.assembler.command.update.OperationLogUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.sys_operation_log.controller.assembler.resource.OperationLogResourceAssembler;
import com.tecpie.platform.user.business.system.sys_operation_log.entity.OperationLog;
import com.tecpie.platform.user.business.system.sys_operation_log.service.OperationLogService;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import com.github.pagehelper.page.PageMethod;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志表 控制器实现
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/operationLog")
public class OperationLogRestController extends BaseController<OperationLogService, OperationLog, OperationLogResource> implements OperationLogRestApi {

  /**
   * 根据ID查询详情信息
   */
  @Override
  @GetMapping("/{id}")
  public Result<OperationLogResource> getOperationLog(@PathVariable("id") Serializable id) {
    OperationLog entity = this.baseService.selectExtensionById(id);
    return Result.success(OperationLogResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Override
  @PostMapping("/list")
  public Result<List<OperationLogResource>> searchOperationLogList(@RequestBody OperationLogQueryCommand command) {
    List<OperationLog> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(OperationLogResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Override
  @PostMapping("/page")
  public Result<PagedResult<OperationLogResource>> searchOperationLogPage(@RequestBody OperationLogPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(OperationLog.class, command));
    // 查询分页数据
    List<OperationLog> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, OperationLogResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存操作日志表数据
   */
  @Override
  @PostMapping
  public Result<Serializable> saveOperationLog(@Valid @RequestBody OperationLogSaveCommand command) {
    return Result.success(this.baseService.saveOperationLog(OperationLogSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新操作日志表数据
   */
  @Override
  @PutMapping("/{id}")
  public Result updateOperationLogById(@PathVariable("id") Serializable id, @Valid @RequestBody OperationLogUpdateCommand command) {
    this.baseService.updateOperationLog(id, OperationLogUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除操作日志表数据
   */
  @Override
  @DeleteMapping("/{id}")
  public Result deleteOperationLogById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID变更操作日志表状态
   */
  @Override
  @PutMapping("/{id}/status/{status}")
  public Result changeOperationLogStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
    this.baseService.changeOperationLogStatus(id, status);
    return Result.success();
  }

  @Override
  protected OperationLogResource toResource(OperationLog entity) {
    return OperationLogResourceAssembler.INSTANCE.parse(entity);
  }

}