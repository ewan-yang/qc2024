package com.tecpie.platform.user.business.system.organization.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserPageQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserSaveCommand;
import com.tecpie.platform.user.business.organization.api.command.update.PasswordUpdateCommand;
import com.tecpie.platform.user.business.organization.api.command.update.SysUserUpdateCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserResource;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUserSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.update.SysUserUpdateCommandAssembler;
import com.tecpie.platform.user.business.system.organization.controller.assembler.resource.SysUserResourceAssembler;
import com.tecpie.platform.user.business.system.organization.entity.*;
import com.tecpie.platform.user.business.system.organization.service.*;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息表 控制器实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Tag(name = "用户信息表接口定义")
@RestController
@RequestMapping("/api/v1/user")
public class SysUserRestController extends BaseController<SysUserService, SysUser, SysUserResource> {

    private final SysUserRoleService userRoleService;

    private final SysUserUnitService userUnitService;

    private final SysUnitService unitService;

    private final SysRoleService sysRoleService;

    @Autowired
    public SysUserRestController(SysUserRoleService userRoleService,
                                 SysUserUnitService userUnitService,
                                 SysUnitService unitService,
                                 SysRoleService sysRoleService) {
        this.userRoleService = userRoleService;
        this.userUnitService = userUnitService;
        this.unitService = unitService;

        this.sysRoleService = sysRoleService;
    }

    /**
     * 根据ID查询详情信息
     */
    @Operation(summary = "查询详情信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:view')")
    public Result<SysUserResource> getSysUser(@PathVariable("id") Serializable id) {
        SysUser entity = this.baseService.selectExtensionById(id);
        return Result.success(SysUserResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Operation(summary = "检索列表数据")
    @PostMapping("/list")
    public Result<List<SysUserResource>> searchSysUserList(@RequestBody SysUserQueryCommand command) {
        List<SysUser> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(SysUserResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Operation(summary = "检索列表数据")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('user:view')")
    public Result<List<SysUserResource>> searchSysUserList() {
        SysUserQueryCommand command = new SysUserQueryCommand();
        command.setStatus(1);
        return this.searchSysUserList(command);
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Operation(summary = "检索分页列表数据")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('user:view')")
    public Result<PagedResult<SysUserResource>> searchSysUserPage(@RequestBody SysUserPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(),
                ConditionUtil.buildOrderBy(SysUser.class, command));
        // 查询分页数据
        List<SysUser> entityList = this.baseService.searchListByUnit(command.getCondition());
        if (CollectionUtils.isNotEmpty(entityList)) {
            List<Serializable> userIdList = entityList.stream().map(SysUser::getId).collect(Collectors.toList());
            List<SysUnit> sysUnitList = unitService.listByUserIdList(userIdList);
            List<SysRole> sysRoleList = sysRoleService.listByUserIdList(userIdList);
            entityList.forEach(user -> {
                Serializable userId = user.getId();
                user.setUnitList(sysUnitList.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList()));
                user.setRoleList(sysRoleList.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList()));
            });
        }
        // 构造分页结果
        return Result.success(entityList, SysUserResourceAssembler.INSTANCE.parseList(entityList));
    }

    @Operation(summary = "根据Ids获取用户信息")
    @PostMapping("/getUserListByIds")
    public Result<List<SysUserResource>> getUserListByIds(@RequestBody SysUserQueryCommand command) {
        return Result.success(SysUserResourceAssembler.INSTANCE.parseList(this.baseService.getUserListByIds(command.getIds())));
    }

    /**
     * 保存用户信息表数据
     */
    @Operation(summary = "保存用户信息表数据")
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public Result<Serializable> saveSysUser(@Valid @RequestBody SysUserSaveCommand command) {
        return Result.success(this.baseService.saveSysUser(SysUserSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新用户信息表数据
     */
    @Operation(summary = "更新用户信息表数据")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> updateSysUserById(@PathVariable("id") Serializable id,
                                          @Valid @RequestBody SysUserUpdateCommand command) {
        this.baseService.updateSysUser(id, SysUserUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除用户信息表数据
     */
    @Operation(summary = "删除用户信息表数据")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> deleteSysUserById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        userRoleService.lambdaUpdate().eq(SysUserRole::getUserId, id).remove();
        userUnitService.lambdaUpdate().eq(SysUserUnit::getUserId, id).remove();
        UserCacheHelper.removeSecurityUser(id.toString());
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @Operation(summary = "批量删除数据")
    @DeleteMapping("batch/{ids}")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> batchDelete(@PathVariable("ids") List<Serializable> ids) {
        this.baseService.removeByIds(ids);
        userRoleService.lambdaUpdate().in(SysUserRole::getUserId, ids).remove();
        userUnitService.lambdaUpdate().in(SysUserUnit::getUserId, ids).remove();
        UserCacheHelper.clearSecurityUser();
        return Result.success();
    }

    /**
     * 根据ID变更用户信息表状态
     */
    @Operation(summary = "变更用户信息表状态")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> changeSysUserStatusById(@PathVariable("id") Serializable id,
                                                @PathVariable("status") Integer status) {
        this.baseService.changeSysUserStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@RequestBody PasswordUpdateCommand command) {
        this.baseService.updatePassword(command);
        return Result.success();
    }


    @Override
    protected SysUserResource toResource(SysUser entity) {
        return SysUserResourceAssembler.INSTANCE.parse(entity);
    }

}