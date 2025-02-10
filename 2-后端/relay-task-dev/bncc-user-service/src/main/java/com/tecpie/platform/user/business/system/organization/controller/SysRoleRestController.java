package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.user.business.organization.api.command.query.SysRolePageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysRoleQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysRoleSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysRoleUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysRoleResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysRoleSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysRoleUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysRoleResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.service.SysRolePermissionService;
import com.tecpie.platform.user.business.system.organization.service.SysRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色信息表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "角色信息表接口定义")
@RestController
@RequestMapping("/api/v1/role")
public class SysRoleRestController extends BaseController<SysRoleService, SysRole, SysRoleResource> {

    private final SysUserRoleService sysUserRoleService;

    private final SysRolePermissionService sysRolePermissionService;

    @Autowired
    public SysRoleRestController(SysUserRoleService sysUserRoleService,
                                 SysRolePermissionService sysRolePermissionService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysRolePermissionService = sysRolePermissionService;
    }


    /**
     * 根据ID查询详情信息
     */
    @Operation(summary = "查询详情信息")
    @GetMapping("/{id}")
    public Result<SysRoleResource> getSysRole(@PathVariable("id") Serializable id) {
        SysRole entity = this.baseService.selectExtensionById(id);
        return Result.success(SysRoleResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Operation(summary = "检索列表数据")
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('role:view')")
    public Result<List<SysRoleResource>> searchSysRoleList(@RequestBody SysRoleQueryCommand command) {
        List<SysRole> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(SysRoleResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Operation(summary = "检索分页列表数据")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('role:view')")
    public Result<PagedResult<SysRoleResource>> searchSysRolePage(
            @RequestBody SysRolePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(),
                ConditionUtil.buildOrderBy(SysRole.class, command));
        // 查询分页数据
        SysRoleQueryCommand condition = command.getCondition();
        List<SysRole> entityList = this.baseService.lambdaQuery()
                .eq(condition.getStatus() != null, BaseEntity::getStatus, condition.getStatus())
                .like(!StringUtils.isEmpty(condition.getCode()), SysRole::getCode, condition.getCode())
                .like(!StringUtils.isEmpty(condition.getName()), SysRole::getName, condition.getName())
                .list();
        // 构造分页结果
        return Result.success(entityList, SysRoleResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存角色信息表数据
     */
    @Operation(summary = "保存角色信息表数据")
    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    public Result<Serializable> saveSysRole(@Valid @RequestBody SysRoleSaveCommand command) {
        return Result
                .success(this.baseService.saveSysRole(SysRoleSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新角色信息表数据
     */
    @Operation(summary = "更新角色信息表数据")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> updateSysRoleById(@PathVariable("id") Serializable id,
                                          @Valid @RequestBody SysRoleUpdateCommand command) {
        this.baseService.updateSysRole(id, SysRoleUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除角色信息表数据
     */
    @Operation(summary = "删除角色信息表数据")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public Result<Void> deleteSysRoleById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        sysUserRoleService.lambdaUpdate().eq(SysUserRole::getRoleId, id).remove();
        sysRolePermissionService.lambdaUpdate().eq(SysRolePermission::getRoleId, id).remove();
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @Operation(summary = "批量删除数据")
    @DeleteMapping("batch/{ids}")
    @PreAuthorize("hasAuthority('role:delete')")
    public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
        this.baseService.removeByIds(ids);
        sysUserRoleService.lambdaUpdate().in(SysUserRole::getRoleId, ids).remove();
        sysRolePermissionService.lambdaUpdate().in(SysRolePermission::getRoleId, ids).remove();
        return Result.success();
    }

    /**
     * 根据ID变更角色信息表状态
     */
    @Operation(summary = "变更角色信息表状态")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> changeSysRoleStatusById(@PathVariable("id") Serializable id,
                                                @PathVariable("status") Integer status) {
        this.baseService.changeSysRoleStatus(id, status);
        return Result.success();
    }

    @Override
    protected SysRoleResource toResource(SysRole entity) {
        return SysRoleResourceAssembler.INSTANCE.parse(entity);
    }

}