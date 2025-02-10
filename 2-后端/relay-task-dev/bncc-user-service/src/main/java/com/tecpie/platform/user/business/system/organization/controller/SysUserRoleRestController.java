package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserRolePageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserRoleQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserRoleSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysUserRoleUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserRoleResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUserRoleSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysUserRoleUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysUserRoleResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
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
 * 用户角色关联表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "用户角色关联表接口定义")
@RestController
@RequestMapping("/api/v1/userRole")
public class SysUserRoleRestController extends BaseController<SysUserRoleService, SysUserRole, SysUserRoleResource> {

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysUserRoleResource> getSysUserRole(@PathVariable("id") Serializable id) {
    SysUserRole entity = this.baseService.selectExtensionById(id);
    return Result.success(SysUserRoleResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  public Result<List<SysUserRoleResource>> searchSysUserRoleList(@RequestBody SysUserRoleQueryCommand command) {
    List<SysUserRole> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysUserRoleResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  public Result<PagedResult<SysUserRoleResource>> searchSysUserRolePage(@RequestBody SysUserRolePageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysUserRole.class, command));
    // 查询分页数据
    List<SysUserRole> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysUserRoleResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存用户角色关联表数据
   */
  @Operation(summary = "保存用户角色关联表数据")
  @PostMapping
  public Result<Serializable> saveSysUserRole(@Valid @RequestBody SysUserRoleSaveCommand command) {
    return Result.success(this.baseService.saveSysUserRole(SysUserRoleSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新用户角色关联表数据
   */
  @Operation(summary = "更新用户角色关联表数据")
  @PutMapping("/{id}")
  public Result<Void> updateSysUserRoleById(@PathVariable("id") Serializable id,
      @Valid @RequestBody SysUserRoleUpdateCommand command) {
    this.baseService
        .updateSysUserRole(id, SysUserRoleUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 批量更新用户角色关系
   */
  @Operation(summary = "批量更新用户角色关系")
  @PutMapping("batch")
  public Result<Boolean> batchSaveOrUpdate(
      @Valid @RequestBody List<SysUserRoleSaveCommand> commands) {
    return Result.success(this.baseService.saveOrUpdateBatch(commands));
  }

  /**
   * 根据ID删除用户角色关联表数据
   */
  @Operation(summary = "删除用户角色关联表数据")
  @DeleteMapping("/{id}")
  public Result<Void> deleteSysUserRoleById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID变更用户角色关联表状态
   */
  @Operation(summary = "变更用户角色关联表状态")
  @PutMapping("/{id}/status/{status}")
  public Result<Void> changeSysUserRoleStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
      this.baseService.changeSysUserRoleStatus(id, status);
      return Result.success();
  }

  @Override
  protected SysUserRoleResource toResource(SysUserRole entity) {
      return SysUserRoleResourceAssembler.INSTANCE.parse(entity);
  }

}