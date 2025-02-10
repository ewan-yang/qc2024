package com.tecpie.platform.user.business.system.function.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.function.api.command.query.SysPermissionPageQueryCommand;
import com.tecpie.platform.user.business.function.api.command.query.SysPermissionQueryCommand;
import com.tecpie.platform.user.business.function.api.command.save.SysPermissionSaveCommand;
import com.tecpie.platform.user.business.function.api.command.update.SysPermissionUpdateCommand;
import com.tecpie.platform.user.business.function.api.resource.SysPermissionResource;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.save.SysPermissionSaveCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.update.SysPermissionUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.resource.SysPermissionResourceAssembler;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.service.SysPermissionService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
 * 系统权限表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Tag(name = "系统权限表接口定义")
@RestController
@RequestMapping("/api/v1/permission")
public class SysPermissionRestController extends BaseController<SysPermissionService, SysPermission, SysPermissionResource> {

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('permission:view')")
  public Result<SysPermissionResource> getSysPermission(@PathVariable("id") Serializable id) {
    SysPermission entity = this.baseService.selectExtensionById(id);
    return Result.success(SysPermissionResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  @PreAuthorize("hasAuthority('permission:view')")
  public Result<List<SysPermissionResource>> searchSysPermissionList(@RequestBody SysPermissionQueryCommand command) {
    List<SysPermission> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysPermissionResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('permission:view')")
  public Result<PagedResult<SysPermissionResource>> searchSysPermissionPage(@RequestBody SysPermissionPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysPermission.class, command));
    // 查询分页数据
    List<SysPermission> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysPermissionResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存系统权限表数据
   */
  @Operation(summary = "保存系统权限表数据")
  @PostMapping
  @PreAuthorize("hasAuthority('permission:add')")
  public Result<Serializable> saveSysPermission(@Valid @RequestBody SysPermissionSaveCommand command) {
    return Result.success(this.baseService.saveSysPermission(SysPermissionSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新系统权限表数据
   */
  @Operation(summary = "更新系统权限表数据")
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('permission:update')")
  public Result<Void> updateSysPermissionById(@PathVariable("id") Serializable id, @Valid @RequestBody SysPermissionUpdateCommand command) {
    this.baseService.updateSysPermission(id, SysPermissionUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除系统权限表数据
   */
  @Operation(summary = "删除系统权限表数据")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('permission:delete')")
  public Result<Void> deleteSysPermissionById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 批量删除数据
   */
  @Operation(summary = "批量删除数据")
  @DeleteMapping("batch/{ids}")
  @PreAuthorize("hasAuthority('permission:delete')")
  public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
    this.baseService.removeByIds(ids);
    return Result.success();
  }

  /**
   * 根据ID变更系统权限表状态
   */
  @Operation(summary = "变更系统权限表状态")
  @PutMapping("/{id}/status/{status}")
  @PreAuthorize("hasAuthority('permission:update')")
  public Result<Void> changeSysPermissionStatusById(@PathVariable("id") Serializable id,
      @PathVariable("status") Integer status) {
    this.baseService.changeSysPermissionStatus(id, status);
    return Result.success();
  }

  @Override
  protected SysPermissionResource toResource(SysPermission entity) {
    return SysPermissionResourceAssembler.INSTANCE.parse(entity);
  }

}