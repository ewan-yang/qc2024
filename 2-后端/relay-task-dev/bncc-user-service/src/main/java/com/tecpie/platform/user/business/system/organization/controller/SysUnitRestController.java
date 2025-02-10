package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.util.tree.TecpieTreeUtil;
import com.tecpie.platform.user.business.organization.api.command.query.SysUnitPageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUnitSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysUnitUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUnitResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUnitSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysUnitUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysUnitResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysUnit;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.service.SysUnitService;
import com.tecpie.platform.user.business.system.organization.service.SysUserUnitService;
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
 * 组织结构表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "组织结构表接口定义")
@RestController
@RequestMapping("/api/v1/unit")
public class SysUnitRestController extends BaseController<SysUnitService, SysUnit, SysUnitResource> {

    private final SysUserUnitService userUnitService;

    @Autowired
    public SysUnitRestController(SysUserUnitService userUnitService) {
        this.userUnitService = userUnitService;
    }

    /**
     * 根据ID查询详情信息
     */
    @Operation(summary = "查询详情信息")
    @GetMapping("/{id}")
    public Result<SysUnitResource> getSysUnit(@PathVariable("id") Serializable id) {
        SysUnit entity = this.baseService.selectExtensionById(id);
        return Result.success(SysUnitResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Operation(summary = "检索列表数据")
    @PostMapping("/list")
    public Result<List<SysUnitResource>> searchSysUnitList(@RequestBody SysUnitQueryCommand command) {
        return Result.success(this.baseService.searchExtensionPageList(command));
    }

    /**
     * 获取组织结构树
     */
    @Operation(summary = "获取组织结构树")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('unit:view')")
    public Result<List<SysUnitResource>> getUnitTree(@RequestBody SysUnitQueryCommand command) {
        List<SysUnitResource> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TecpieTreeUtil.buildTree(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Operation(summary = "检索分页列表数据")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('unit:view')")
    public Result<PagedResult<SysUnitResource>> searchSysUnitPage(
            @RequestBody SysUnitPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(),
                ConditionUtil.buildOrderBy(SysUnit.class, command));
        // 查询分页数据
        List<SysUnitResource> entityList = this.baseService
                .searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, entityList);
    }

    /**
     * 保存组织结构表数据
     */
    @Operation(summary = "保存组织结构表数据")
    @PostMapping
    @PreAuthorize("hasAuthority('unit:add')")
    public Result<Serializable> saveSysUnit(@Valid @RequestBody SysUnitSaveCommand command) {
        return Result.success(this.baseService.saveSysUnit(SysUnitSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新组织结构表数据
     */
    @Operation(summary = "更新组织结构表数据")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('unit:update')")
    public Result<Void> updateSysUnitById(@PathVariable("id") Serializable id, @Valid @RequestBody SysUnitUpdateCommand command) {
        this.baseService.updateSysUnit(id, SysUnitUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除组织结构表数据
     */
    @Operation(summary = "删除组织结构表数据")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('unit:delete')")
    public Result<Void> deleteSysUnitById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        this.baseService.lambdaUpdate().eq(SysUnit::getParentId, id).set(SysUnit::getParentId, null)
                .update();
        userUnitService.lambdaUpdate().eq(SysUserUnit::getUserId, id).remove();
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @Operation(summary = "批量删除数据")
    @DeleteMapping("batch/{ids}")
    @PreAuthorize("hasAuthority('unit:delete')")
    public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
        this.baseService.removeByIds(ids);
        this.baseService.lambdaUpdate().in(SysUnit::getParentId, ids).set(SysUnit::getParentId, null)
                .update();
        userUnitService.lambdaUpdate().in(SysUserUnit::getUserId, ids).remove();
        return Result.success();
    }

    /**
     * 根据ID变更组织结构表状态
     */
    @Operation(summary = "变更组织结构表状态")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('unit:update')")
    public Result<Void> changeSysUnitStatusById(@PathVariable("id") Serializable id,
                                                @PathVariable("status") Integer status) {
        this.baseService.changeSysUnitStatus(id, status);
        return Result.success();
    }

    @Override
    protected SysUnitResource toResource(SysUnit entity) {
        return SysUnitResourceAssembler.INSTANCE.parse(entity);
    }

}