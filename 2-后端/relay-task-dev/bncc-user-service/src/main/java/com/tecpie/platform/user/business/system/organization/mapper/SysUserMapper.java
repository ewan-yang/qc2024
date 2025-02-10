package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.starter.feign.entity.SecurityUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return SysUser
     */
    SysUser selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<SysUser>
     */
    List<SysUser> searchExtensionPageList(@Param("condition") SysUserQueryCommand command);

    List<SysUser> searchListByUnit(@Param("condition") SysUserQueryCommand condition);

    SysUser selectAuthByCode(@Param("code") String code);

    SecurityUser selectSecurityUser(@Param("id") Serializable id);

    List<SysUser> selectByIds(@Param("ids") List<Long> ids);
}