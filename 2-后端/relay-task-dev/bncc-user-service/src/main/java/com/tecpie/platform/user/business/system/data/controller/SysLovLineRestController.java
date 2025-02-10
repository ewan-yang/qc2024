package com.tecpie.platform.user.business.system.data.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.data.api.command.query.SysLovLinePageQueryCommand;
import com.tecpie.platform.user.business.data.api.command.query.SysLovLineQueryCommand;
import com.tecpie.platform.user.business.data.api.command.save.SysLovLineSaveCommand;
import com.tecpie.platform.user.business.data.api.command.update.SysLovLineUpdateCommand;
import com.tecpie.platform.user.business.data.api.resource.SysLovLineResource;
import com.tecpie.platform.user.business.system.data.controller.assembler.command.save.SysLovLineSaveCommandAssembler;
import com.tecpie.platform.user.business.system.data.controller.assembler.command.update.SysLovLineUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.data.controller.assembler.resource.SysLovLineResourceAssembler;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
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
 * LOV明细行 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "LOV明细行接口定义")
@RestController
@RequestMapping("/api/v1/lovLine")
public class SysLovLineRestController extends BaseController<SysLovLineService, SysLovLine, SysLovLineResource> {

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysLovLineResource> getSysLovLine(@PathVariable("id") Serializable id) {
    SysLovLine entity = this.baseService.selectExtensionById(id);
    return Result.success(SysLovLineResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  public Result<List<SysLovLineResource>> searchSysLovLineList(@RequestBody SysLovLineQueryCommand command) {
    List<SysLovLine> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysLovLineResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  public Result<PagedResult<SysLovLineResource>> searchSysLovLinePage(@RequestBody SysLovLinePageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysLovLine.class, command));
    // 查询分页数据
    List<SysLovLine> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysLovLineResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存LOV明细行数据
   */
  @Operation(summary = "保存LOV明细行数据")
  @PostMapping
  public Result<Serializable> saveSysLovLine(@Valid @RequestBody SysLovLineSaveCommand command) {
    return Result.success(this.baseService.saveSysLovLine(SysLovLineSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新LOV明细行数据
   */
  @Operation(summary = "更新LOV明细行数据")
  @PutMapping("/{id}")
  public Result<Void> updateSysLovLineById(@PathVariable("id") Serializable id, @Valid @RequestBody SysLovLineUpdateCommand command) {
    this.baseService.updateSysLovLine(id, SysLovLineUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除LOV明细行数据
   */
  @Operation(summary = "删除LOV明细行数据")
  @DeleteMapping("/{id}")
  public Result<Void> deleteSysLovLineById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID变更LOV明细行状态
   */
  @Operation(summary = "变更LOV明细行状态")
  @PutMapping("/{id}/status/{status}")
  public Result<Void> changeSysLovLineStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
      this.baseService.changeSysLovLineStatus(id, status);
      return Result.success();
  }

  @Override
  protected SysLovLineResource toResource(SysLovLine entity) {
      return SysLovLineResourceAssembler.INSTANCE.parse(entity);
  }

}