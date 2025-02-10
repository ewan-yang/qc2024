package com.tecpie.platform.user.security.isc.service;

import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.util.cache.TecpieCacheUtil;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.service.SysRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.constants.SecurityConstant;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.platform.user.security.igw.util.IgwUtils;
import com.tecpie.platform.user.security.isc.entity.IscUser;
import com.tecpie.platform.user.utils.SecurityUtils;
import com.tecpie.starter.feign.entity.SecurityToken;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * isc登录授权Service类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @Author Tanzj
 * @Date 2023/06/01
 */
@Slf4j
@Service
public class IscService {

    @Value("${isc.profileUrl}")
    private String profileUrl;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SecurityUtils securityUtils;

    private IscUser login(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(profileUrl.concat("?access_token=".concat(accessToken)), IscUser.class);
    }

    public Map<String, Object> oauth(String client, String accessToken) {
        log.info("accessToken:" + accessToken);
        // 获取用户信息
        IscUser user = login(accessToken);
        // 用户数据对应系统用户
        SysUser sysUser = userService.selectAuthByCode(user.getIscUserSourceId());
        if (ObjectUtils.isEmpty(sysUser)) {
            sysUser.setName(user.getName());
            userService.save(sysUser);
            roleService.lambdaQuery().eq(SysRole::getCode, "ptyh").oneOpt().ifPresent(ptyh -> {
                //默认角色权限
                SysRole sysRole = new SysRole();
                sysRole.setCode("ptyh");
                sysUser.setRoleList(Collections.singletonList(sysRole));
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(ptyh.getId());
                sysUserRole.setUserId(sysUser.getId());
                userRoleService.save(sysUserRole);
            });
        }
        SysUserDetails sysUserDetail = new SysUserDetails(sysUser);
        sysUserDetail.setWorkNumber(user.getIscUserId());
        SecurityToken securityToken = securityUtils.generateTokenPair(client, sysUser.getId());
        UserCacheHelper.removeSecurityUser(sysUser.getId().toString());
        UserCacheHelper.setSecurityUser(userService.selectSecurityUser(sysUser.getId()));
        //整合数据并返回
        sysUserDetail.setToken(securityToken.getToken());
        sysUserDetail.setRefreshToken(securityToken.getRefreshToken());
        return SecurityUtils.convertUser(sysUserDetail);
    }

}
