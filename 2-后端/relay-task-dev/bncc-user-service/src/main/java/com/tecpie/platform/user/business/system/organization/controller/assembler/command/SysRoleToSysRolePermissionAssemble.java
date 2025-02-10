package com.tecpie.platform.user.business.system.organization.controller.assembler.command;

import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;

import java.util.List;
import java.util.stream.Collectors;

public class SysRoleToSysRolePermissionAssemble {

    /**
     * 根据 SysRole 中的 userList 及其自身 id，将其转化为 List<SysUserRole>
     *
     * @param sysRole sysRole
     * @return List<SysRolePermission>
     */
    public static List<SysRolePermission> parse(SysRole sysRole) {
        return sysRole.getPermissionList().stream().map(sysPermission -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(sysRole.getId());
            sysRolePermission.setPermissionId(sysPermission.getId());
            return sysRolePermission;
        }).collect(Collectors.toList());
    }

}