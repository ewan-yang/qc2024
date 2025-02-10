package com.tecpie.platform.user.business.system.function.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.function.api.command.query.SysOperationPageQueryCommand;
import com.tecpie.platform.user.business.function.api.command.query.SysOperationQueryCommand;
import com.tecpie.platform.user.business.function.api.command.save.SysOperationSaveCommand;
import com.tecpie.platform.user.business.function.api.command.update.SysOperationUpdateCommand;
import com.tecpie.platform.user.business.function.api.resource.SysOperationResource;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.save.SysOperationSaveCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.update.SysOperationUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.resource.SysOperationResourceAssembler;
import com.tecpie.platform.user.business.system.function.entity.SysOperation;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.service.SysOperationService;
import com.tecpie.platform.user.business.system.function.service.SysPermissionService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作信息表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Tag(name = "操作信息表接口定义")
@RestController
@RequestMapping("/api/v1/operation")
public class SysOperationRestController extends
    BaseController<SysOperationService, SysOperation, SysOperationResource> {

  @Autowired
  private SysPermissionService sysPermissionService;

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('operation:view')")
  public Result<SysOperationResource> getSysOperation(@PathVariable("id") Serializable id) {
    SysOperation entity = this.baseService.selectExtensionById(id);
    return Result.success(SysOperationResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  @PreAuthorize("hasAuthority('operation:view')")
  public Result<List<SysOperationResource>> searchSysOperationList(@RequestBody SysOperationQueryCommand command) {
    List<SysOperation> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysOperationResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('operation:view')")
  public Result<PagedResult<SysOperationResource>> searchSysOperationPage(@RequestBody SysOperationPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysOperation.class, command));
    // 查询分页数据
    List<SysOperation> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysOperationResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存操作信息表数据
   */
  @Operation(summary = "保存操作信息表数据")
  @PostMapping
  @PreAuthorize("hasAuthority('operation:add')")
  public Result<Serializable> saveSysOperation(@Valid @RequestBody SysOperationSaveCommand command) {
    return Result.success(this.baseService.saveSysOperation(SysOperationSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新操作信息表数据
   */
  @Operation(summary = "更新操作信息表数据")
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('operation:update')")
  public Result<Void> updateSysOperationById(@PathVariable("id") Serializable id, @Valid @RequestBody SysOperationUpdateCommand command) {
    this.baseService.updateSysOperation(id, SysOperationUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除操作信息表数据
   */
  @Operation(summary = "删除操作信息表数据")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('operation:delete')")
  public Result<Void> deleteSysOperationById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    sysPermissionService.lambdaUpdate().eq(SysPermission::getResourceId, id).remove();
    return Result.success();
  }

  /**
   * 批量删除数据
   */
  @Operation(summary = "批量删除数据")
  @DeleteMapping("batch/{ids}")
  @PreAuthorize("hasAuthority('operation:delete')")
  public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
    this.baseService.removeByIds(ids);
    sysPermissionService.lambdaUpdate().in(SysPermission::getResourceId, ids).remove();
    return Result.success();
  }

  /**
   * 根据ID变更操作信息表状态
   */
  @Operation(summary = "变更操作信息表状态")
  @PutMapping("/{id}/status/{status}")
  @PreAuthorize("hasAuthority('operation:update')")
  public Result<Void> changeSysOperationStatusById(@PathVariable("id") Serializable id,
      @PathVariable("status") Integer status) {
    this.baseService.changeSysOperationStatus(id, status);
    return Result.success();
  }

  @Override
  protected SysOperationResource toResource(SysOperation entity) {
    return SysOperationResourceAssembler.INSTANCE.parse(entity);
  }

}