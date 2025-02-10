package com.tecpie.platform.user.security.igw.util;

import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.util.cache.TecpieCacheUtil;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.constants.SecurityConstant;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.platform.user.utils.SecurityUtils;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * i国网 工具类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @Author Tanzj
 * @Date 2023/06/06
 */
@Slf4j
public class IgwUtils {

    /**
     * 清空缓存并返回新的认证用户
     */
    public static Map<String, Object> getSecurityUser(SysUserDetails sysUserDetail, SysUserService userService, int tokenExpireTime) {
        // 生成token
        String token = Jwts.builder()
                //主题 放入用户id
                .setSubject(String.valueOf(sysUserDetail.getId()))
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 60 * 1000L))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                .compact();
        log.error("生成的Token", token);
        sysUserDetail.setToken(token);
        try {
            String cacheKey = String.format("%s:%s", SecurityConstant.CACHE_KEY_OF_TOKEN, sysUserDetail.getId());
            TecpieCacheUtil.removeCommon(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
            TecpieCacheUtil.putCommon(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey, sysUserDetail.getToken(), tokenExpireTime * 60L, TimeUnit.SECONDS);
            UserCacheHelper.removeSecurityUser(sysUserDetail.getId().toString());
            UserCacheHelper.setSecurityUser(userService.selectSecurityUser(sysUserDetail.getId()));
        } catch (Exception e) {
            log.error("redis操作失败", e);
            userService.lambdaUpdate().eq(BaseEntity::getId, sysUserDetail.getId())
                    .set(SysUser::getToken, sysUserDetail.getToken()).set(SysUser::getLoginSuccessDate,
                    LocalDateTime.now()).update();
        }
        // 整合数据并返回
        return SecurityUtils.convertUser(sysUserDetail);
    }

}
