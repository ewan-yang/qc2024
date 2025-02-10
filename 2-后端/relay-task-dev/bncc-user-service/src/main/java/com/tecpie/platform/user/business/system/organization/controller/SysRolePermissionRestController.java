package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.organization.api.command.query.SysRolePermissionPageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysRolePermissionQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysRolePermissionSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysRolePermissionUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysRolePermissionResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysRolePermissionSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysRolePermissionUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysRolePermissionResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import com.tecpie.platform.user.business.system.organization.service.SysRolePermissionService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色与系统权限关联表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "角色与系统权限关联表接口定义")
@RestController
@RequestMapping("/api/v1/rolePermission")
public class SysRolePermissionRestController extends BaseController<SysRolePermissionService, SysRolePermission, SysRolePermissionResource> {

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysRolePermissionResource> getSysRolePermission(@PathVariable("id") Serializable id) {
    SysRolePermission entity = this.baseService.selectExtensionById(id);
    return Result.success(SysRolePermissionResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  public Result<List<SysRolePermissionResource>> searchSysRolePermissionList(@RequestBody SysRolePermissionQueryCommand command) {
    List<SysRolePermission> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysRolePermissionResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  public Result<PagedResult<SysRolePermissionResource>> searchSysRolePermissionPage(@RequestBody SysRolePermissionPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysRolePermission.class, command));
    // 查询分页数据
    List<SysRolePermission> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysRolePermissionResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存角色与系统权限关联表数据
   */
  @Operation(summary = "保存角色与系统权限关联表数据")
  @PostMapping
  public Result<Serializable> saveSysRolePermission(@Valid @RequestBody SysRolePermissionSaveCommand command) {
    return Result.success(this.baseService.saveSysRolePermission(SysRolePermissionSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新角色与系统权限关联表数据
   */
  @Operation(summary = "更新角色与系统权限关联表数据")
  @PutMapping("/{id}")
  public Result<Void> updateSysRolePermissionById(@PathVariable("id") Serializable id, @Valid @RequestBody SysRolePermissionUpdateCommand command) {
    this.baseService.updateSysRolePermission(id, SysRolePermissionUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除角色与系统权限关联表数据
   */
  @Operation(summary = "删除角色与系统权限关联表数据")
  @DeleteMapping("/{id}")
  public Result<Void> deleteSysRolePermissionById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID变更角色与系统权限关联表状态
   */
  @Operation(summary = "变更角色与系统权限关联表状态")
  @PutMapping("/{id}/status/{status}")
  public Result<Void> changeSysRolePermissionStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
      this.baseService.changeSysRolePermissionStatus(id, status);
      return Result.success();
  }

  @Override
  protected SysRolePermissionResource toResource(SysRolePermission entity) {
      return SysRolePermissionResourceAssembler.INSTANCE.parse(entity);
  }

}