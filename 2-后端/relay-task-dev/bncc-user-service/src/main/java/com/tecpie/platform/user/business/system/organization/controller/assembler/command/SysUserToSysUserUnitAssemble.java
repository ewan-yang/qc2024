package com.tecpie.platform.user.business.system.organization.controller.assembler.command;

import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;

import java.util.List;
import java.util.stream.Collectors;

public class SysUserToSysUserUnitAssemble {

    /**
     * 根据 SysUser 中的 unitList 及其自身 id，将其转化为 List<SysUserUnit>
     *
     * @param sysUser sysUser
     * @return List<SysUserUnit>
     */
    public static List<SysUserUnit> parse(SysUser sysUser) {
        return sysUser.getUnitList().stream().map(sysUnit -> {
            SysUserUnit sysUserUnit = new SysUserUnit();
            sysUserUnit.setUnitId(sysUnit.getId());
            sysUserUnit.setUserId(sysUser.getId());
            return sysUserUnit;
        }).collect(Collectors.toList());
    }

}