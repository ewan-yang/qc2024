package com.tecpie.platform.user.business.system.data.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.data.api.SysLovRestApi;
import com.tecpie.platform.user.business.data.api.command.query.SysLovPageQueryCommand;
import com.tecpie.platform.user.business.data.api.command.query.SysLovQueryCommand;
import com.tecpie.platform.user.business.data.api.command.save.SysLovSaveCommand;
import com.tecpie.platform.user.business.data.api.command.update.SysLovUpdateCommand;
import com.tecpie.platform.user.business.data.api.resource.SysLovResource;
import com.tecpie.platform.user.business.system.data.controller.assembler.command.save.SysLovSaveCommandAssembler;
import com.tecpie.platform.user.business.system.data.controller.assembler.command.update.SysLovUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.data.controller.assembler.resource.SysLovResourceAssembler;
import com.tecpie.platform.user.business.system.data.entity.SysLov;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
import com.tecpie.platform.user.business.system.data.service.SysLovService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * LOV定义表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "LOV定义表接口定义")
@RestController
@RequestMapping("/api/v1/lov")
public class SysLovRestController extends BaseController<SysLovService, SysLov, SysLovResource> implements SysLovRestApi {

    @Resource
    private SysLovLineService sysLovLineService;

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysLovResource> getSysLov(@PathVariable("id") Serializable id) {
    SysLov entity = this.baseService.selectExtensionById(id);
    return Result.success(SysLovResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  public Result<List<SysLovResource>> searchSysLovList(@RequestBody SysLovQueryCommand command) {
    List<SysLov> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysLovResourceAssembler.INSTANCE.parseList(entityList));
  }

    /**
     * 根据Code获取字典数据
     */
    @Override
    @Operation(summary = "根据Code获取字典数据")
    @GetMapping("/searchList/{lovCode}")
    public Result<List<SysLovResource>> searchListByCode(@PathVariable("lovCode") String lovCode) {
        List<SysLov> entityList = this.baseService.searchListByCode(lovCode);
        return Result.success(SysLovResourceAssembler.INSTANCE.parseList(entityList));
    }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('lov:view')")
  public Result<PagedResult<SysLovResource>> searchSysLovPage(@RequestBody SysLovPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SysLov.class, command));
    // 查询分页数据
    List<SysLov> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    // 构造分页结果
    return Result.success(entityList, SysLovResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 保存LOV定义表数据
   */
  @Operation(summary = "保存LOV定义表数据")
  @PostMapping
  public Result<Serializable> saveSysLov(@Valid @RequestBody SysLovSaveCommand command) {
    return Result.success(this.baseService.saveSysLov(SysLovSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新LOV定义表数据
   */
  @Operation(summary = "更新LOV定义表数据")
  @PutMapping("/{id}")
  public Result<Void> updateSysLovById(@PathVariable("id") Serializable id, @Valid @RequestBody SysLovUpdateCommand command) {
    this.baseService.updateSysLov(id, SysLovUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID逻辑删除LOV定义及明细数据
   */
  @Operation(summary = "逻辑删除LOV定义表数据")
  @DeleteMapping("/{id}")
  public Result<Void> removeSysLovById(@PathVariable("id") Serializable id) {
    this.baseService.logicRemoveById(id);
    return Result.success();
  }

  /**
   * 根据ID批量删除
   */
  @Operation(summary = "批量逻辑删除数据")
  @DeleteMapping("batch/{id}")
  public Result<Void> removeSysLovById(@PathVariable("id") List<Serializable> ids) {
    this.baseService.logicRemoveByIds(ids);
    return Result.success();
  }

  /**
   * 根据ID变更LOV定义表状态
   */
  @Operation(summary = "变更LOV定义表状态")
  @PutMapping("/{id}/status/{status}")
  public Result<Void> changeSysLovStatusById(@PathVariable("id") Serializable id,
      @PathVariable("status") Integer status) {
    this.baseService.changeSysLovStatus(id, status);
      return Result.success();
  }

  @Override
  protected SysLovResource toResource(SysLov entity) {
      return SysLovResourceAssembler.INSTANCE.parse(entity);
  }

}