package com.tecpie.platform.user.business.system.function.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.util.tree.TecpieTreeUtil;
import com.tecpie.platform.user.business.function.api.command.query.SysMenuPageQueryCommand;
import com.tecpie.platform.user.business.function.api.command.query.SysMenuQueryCommand;
import com.tecpie.platform.user.business.function.api.command.save.SysMenuSaveCommand;
import com.tecpie.platform.user.business.function.api.command.update.SysMenuUpdateCommand;
import com.tecpie.platform.user.business.function.api.resource.SysMenuResource;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.save.SysMenuSaveCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.command.update.SysMenuUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.function.controller.assembler.resource.SysMenuResourceAssembler;
import com.tecpie.platform.user.business.system.function.entity.SysMenu;
import com.tecpie.platform.user.business.system.function.service.SysMenuService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
 *  控制器实现
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Slf4j
@Tag(name = "菜单接口定义")
@RestController
@RequestMapping("/api/v1/menu")
public class SysMenuRestController extends BaseController<SysMenuService, SysMenu, SysMenuResource> {

  /**
   * @return 返回父节点为空的根菜单（包含其子节点）
   */
  @Operation(summary = "生成菜单列表")
  @GetMapping("/format")
  public Result<List<SysMenuResource>> format() {
    return Result.success(TecpieTreeUtil
        .buildTree(SysMenuResourceAssembler.INSTANCE.parseList(this.baseService.lambdaQuery()
                .eq(SysMenu::getDeleted, 0).eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSort).list())));
  }

  /**
   * 根据ID查询详情信息
   */
  @Operation(summary = "查询详情信息")
  @GetMapping("/{id}")
  public Result<SysMenuResource> getSysMenu(@PathVariable("id") Serializable id) {
    SysMenu entity = this.baseService.selectExtensionById(id);
    return Result.success(SysMenuResourceAssembler.INSTANCE.parse(entity));
  }

  /**
   * 根据筛选条件检索列表数据
   */
  @Operation(summary = "检索列表数据")
  @PostMapping("/list")
  @PreAuthorize("hasAuthority('menu:view')")
  public Result<List<SysMenuResource>> searchSysMenuList(@RequestBody SysMenuQueryCommand command) {
    List<SysMenu> entityList = this.baseService.searchExtensionPageList(command);
    return Result.success(SysMenuResourceAssembler.INSTANCE.parseList(entityList));
  }

  /**
   * 根据筛选条件检索分页列表数据
   */
  @Operation(summary = "检索分页列表数据")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('menu:view')")
  public Result<PagedResult<SysMenuResource>> searchSysMenuPage(@RequestBody SysMenuPageQueryCommand command) {
    // 开启分页切面
    PageMethod.startPage(command.getPageIndex(), command.getPageSize(),
        ConditionUtil.buildOrderBy(SysMenu.class, command));
    // 查询分页数据
    List<SysMenu> entityList = this.baseService.searchExtensionPageList(command.getCondition());
    List<SysMenuResource> sysMenuResources = SysMenuResourceAssembler.INSTANCE
        .parseList(entityList);
    Map<Serializable, String> menuMap = this.baseService.lambdaQuery()
        .in(BaseEntity::getId, sysMenuResources.stream().map(SysMenuResource::getParentId).collect(
            Collectors.toSet())).list().stream()
        .collect(Collectors.toMap(BaseEntity::getId, SysMenu::getName));
    for (SysMenuResource menuResource : sysMenuResources) {
      menuResource.setParentName(menuMap.get(menuResource.getParentId()));
    }
    // 构造分页结果
    return Result.success(entityList, sysMenuResources);
  }

  /**
   * 保存数据
   */
  @Operation(summary = "保存数据")
  @PostMapping
  @PreAuthorize("hasAuthority('menu:add')")
  public Result<Serializable> saveSysMenu(@Valid @RequestBody SysMenuSaveCommand command) {
    return Result.success(this.baseService.saveSysMenu(SysMenuSaveCommandAssembler.INSTANCE.parse(command)));
  }

  /**
   * 根据ID更新数据
   */
  @Operation(summary = "更新数据")
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('menu:update')")
  public Result<Void> updateSysMenuById(@PathVariable("id") Serializable id, @Valid @RequestBody SysMenuUpdateCommand command) {
    this.baseService.updateSysMenu(id, SysMenuUpdateCommandAssembler.INSTANCE.parse(command));
    return Result.success();
  }

  /**
   * 根据ID删除数据
   */
  @Operation(summary = "删除数据")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('menu:delete')")
  public Result<Void> deleteSysMenuById(@PathVariable("id") Serializable id) {
    this.baseService.removeById(id);
    return Result.success();
  }

  /**
   * 根据ID删除数据
   */
  @Operation(summary = "删除数据")
  @DeleteMapping("batch/{ids}")
  @PreAuthorize("hasAuthority('menu:delete')")
  public Result<Void> deleteSysMenuByIds(@PathVariable("ids") List<Serializable> ids) {
    this.baseService.removeByIds(ids);
    return Result.success();
  }

  /**
   * 根据ID变更状态
   */
  @Operation(summary = "变更状态")
  @PutMapping("/{id}/status/{status}")
  @PreAuthorize("hasAuthority('menu:update')")
  public Result<Void> changeSysMenuStatusById(@PathVariable("id") Serializable id,
      @PathVariable("status") Integer status) {
    this.baseService.changeSysMenuStatus(id, status);
    return Result.success();
  }

  @Override
  protected SysMenuResource toResource(SysMenu entity) {
    return SysMenuResourceAssembler.INSTANCE.parse(entity);
  }

}