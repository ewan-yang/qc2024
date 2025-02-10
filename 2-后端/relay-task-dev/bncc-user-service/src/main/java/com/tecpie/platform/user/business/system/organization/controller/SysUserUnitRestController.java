package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserUnitPageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserUnitSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysUserUnitUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserUnitResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUserUnitSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysUserUnitUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysUserUnitResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.service.SysUserUnitService;
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
 * 用户组织关系表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "用户组织关系表接口定义")
@RestController
@RequestMapping("/api/v1/userUnit")
public class SysUserUnitRestController extends BaseController<SysUserUnitService, SysUserUnit, SysUserUnitResource> {

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysUserUnitResource> getSysUserUnit(@PathVariable("id") Serializable id) {
    SysUserUnit entity = this.baseService.selectExtensionById(id);
    return Result.success(SysUserUnitResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  public Result<List<SysUserUnitResource>> searchSysUserUnitList(@RequestBody SysUserUnitQueryCommand command) {
    List<SysUserUnit> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysUserUnitResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  public Result<PagedResult<SysUserUnitResource>> searchSysUserUnitPage(@RequestBody SysUserUnitPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysUserUnit.class, command));
    // 查询分页数据
    List<SysUserUnit> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysUserUnitResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存用户组织关系表数据
   */
  @Operation(summary = "保存用户组织关系表数据")
  @PostMapping
  public Result<Serializable> saveSysUserUnit(@Valid @RequestBody SysUserUnitSaveCommand command) {
    return Result.success(this.baseService.saveSysUserUnit(SysUserUnitSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新用户组织关系表数据
   */
  @Operation(summary = "更新用户组织关系表数据")
  @PutMapping("/{id}")
  public Result<Void> updateSysUserUnitById(@PathVariable("id") Serializable id,
      @Valid @RequestBody SysUserUnitUpdateCommand command) {
    this.baseService
        .updateSysUserUnit(id, SysUserUnitUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 批量更新用户组织关系
   */
  @Operation(summary = "批量更新用户组织关系")
  @PutMapping("batch")
  public Result<Boolean> batchSaveOrUpdate(
      @Valid @RequestBody List<SysUserUnitSaveCommand> commands) {
    return Result.success(this.baseService.saveOrUpdateBatch(commands));
  }

  /**
   * 根据ID删除用户组织关系表数据
   */
  @Operation(summary = "删除用户组织关系表数据")
  @DeleteMapping("/{id}")
  public Result<Void> deleteSysUserUnitById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID变更用户组织关系表状态
   */
  @Operation(summary = "变更用户组织关系表状态")
  @PutMapping("/{id}/status/{status}")
  public Result<Void> changeSysUserUnitStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
      this.baseService.changeSysUserUnitStatus(id, status);
      return Result.success();
  }

  @Override
  protected SysUserUnitResource toResource(SysUserUnit entity) {
      return SysUserUnitResourceAssembler.INSTANCE.parse(entity);
  }

}