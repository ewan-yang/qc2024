package com.tecpie.platform.user.business.system.function.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.function.api.command.query.SysResourcePageQueryCommand;
import com.tecpie.platform.user.business.function.api.command.query.SysResourceQueryCommand;
import com.tecpie.platform.user.business.function.api.command.save.SysResourceSaveCommand;
import com.tecpie.platform.user.business.function.api.command.update.SysResourceUpdateCommand;
import com.tecpie.platform.user.business.function.api.resource.SysResourceResource;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.save.SysResourceSaveCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.update.SysResourceUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.resource.SysResourceResourceAssembler;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.entity.SysResource;
import com.tecpie.platform.user.business.system.function.service.SysPermissionService;
import com.tecpie.platform.user.business.system.function.service.SysResourceService;
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
 * 系统资源表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Tag(name = "系统资源表接口定义")
@RestController
@RequestMapping("/api/v1/resource")
public class SysResourceRestController extends
    BaseController<SysResourceService, SysResource, SysResourceResource> {

  @Autowired
  private SysPermissionService sysPermissionService;

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('resource:view')")
  public Result<SysResourceResource> getSysResource(@PathVariable("id") Serializable id) {
    SysResource entity = this.baseService.selectExtensionById(id);
    return Result.success(SysResourceResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  @PreAuthorize("hasAuthority('resource:view')")
  public Result<List<SysResourceResource>> searchSysResourceList(@RequestBody SysResourceQueryCommand command) {
    List<SysResource> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysResourceResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('resource:view')")
  public Result<PagedResult<SysResourceResource>> searchSysResourcePage(@RequestBody SysResourcePageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysResource.class, command));
    // 查询分页数据
    List<SysResource> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysResourceResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存系统资源表数据
   */
  @Operation(summary = "保存系统资源表数据")
  @PostMapping
  @PreAuthorize("hasAuthority('resource:add')")
  public Result<Serializable> saveSysResource(@Valid @RequestBody SysResourceSaveCommand command) {
    return Result.success(this.baseService.saveSysResource(SysResourceSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新系统资源表数据
   */
  @Operation(summary = "更新系统资源表数据")
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('resource:update')")
  public Result<Void> updateSysResourceById(@PathVariable("id") Serializable id, @Valid @RequestBody SysResourceUpdateCommand command) {
    this.baseService.updateSysResource(id, SysResourceUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除系统资源表数据
   */
  @Operation(summary = "删除系统资源表数据")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('resource:delete')")
  public Result<Void> deleteSysResourceById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    sysPermissionService.lambdaUpdate().eq(SysPermission::getResourceId, id).remove();
    return Result.success();
  }

  /**
   * 批量删除数据
   */
  @Operation(summary = "批量删除数据")
  @DeleteMapping("batch/{ids}")
  @PreAuthorize("hasAuthority('resource:delete')")
  public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
    this.baseService.removeByIds(ids);
    sysPermissionService.lambdaUpdate().in(SysPermission::getResourceId, ids).remove();
    return Result.success();
  }

  /**
   * 根据ID变更系统资源表状态
   */
  @Operation(summary = "变更系统资源表状态")
  @PutMapping("/{id}/status/{status}")
  @PreAuthorize("hasAuthority('resource:update')")
  public Result<Void> changeSysResourceStatusById(@PathVariable("id") Serializable id,
      @PathVariable("status") Integer status) {
    this.baseService.changeSysResourceStatus(id, status);
    return Result.success();
  }

  @Override
  protected SysResourceResource toResource(SysResource entity) {
    return SysResourceResourceAssembler.INSTANCE.parse(entity);
  }

}