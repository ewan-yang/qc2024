package com.tecpie.platform.user.business.system.organization.controller.assembler.command;

import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;

import java.util.List;
import java.util.stream.Collectors;

public class SysRoleToSysUserRoleAssemble {

    /**
     * 根据 SysRole 中的 userList 及其自身 id，将其转化为 List<SysUserRole>
     *
     * @param sysRole sysRole
     * @return List<SysUserRole>
     */
    public static List<SysUserRole> parse(SysRole sysRole) {
        return sysRole.getUserList().stream().map(sysUser -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(sysRole.getId());
            sysUserRole.setUserId(sysUser.getId());
            return sysUserRole;
        }).collect(Collectors.toList());
    }

}