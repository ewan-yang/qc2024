package com.tecpie.platform.user.business.system.organization.controller.assembler.command;

import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;

import java.util.List;
import java.util.stream.Collectors;

public class SysUserToSysUserRoleAssemble {

    /**
     * 根据 SysUser 中的 roleList 及其自身 id，将其转化为 List<SysUserRole>
     *
     * @param sysUser sysUser
     * @return List<SysUserRole>
     */
    public static List<SysUserRole> parse(SysUser sysUser) {
        return sysUser.getRoleList().stream().map(sysRole -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(sysRole.getId());
            sysUserRole.setUserId(sysUser.getId());
            return sysUserRole;
        }).collect(Collectors.toList());
    }

}