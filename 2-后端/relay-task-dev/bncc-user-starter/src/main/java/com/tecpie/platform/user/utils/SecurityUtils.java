package com.tecpie.platform.user.utils;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.constant.AuthConstants;
import com.tecpie.library.common.constant.CommonConstants;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.util.cache.TecpieCacheUtil;
import com.tecpie.library.common.util.cache.TecpieLockUtil;
import com.tecpie.platform.user.business.system.organization.entity.SysUserToken;
import com.tecpie.platform.user.business.system.organization.service.SysUserTokenService;
import com.tecpie.platform.user.security.auth.model.SysGrantedAuthority;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.starter.feign.entity.SecurityToken;
import com.tecpie.starter.security.support.exception.AuthError;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * Security 工具类
 *
 * @author ZhangDY
 */
@Slf4j
@Component
public class SecurityUtils {

  /**
   * Token失效时间，单位为分钟，默认为30分钟
   */
  @Value("${tecpie.security.tokenExpireTime:30}")
  private Long tokenExpireTime;

  /**
   * Token自动续期时间，单位为分钟，默认为30分钟
   */
  @Value("${tecpie.security.autoRefreshLimit:30}")
  private Long autoRefreshLimit;

  /**
   * RefreshToken失效时间，单位为分钟，默认为480分钟
   */
  @Value("${tecpie.security.refreshTokenExpireTime:480}")
  private Long refreshTokenExpireTime;

  @Value("${tecpie.security.jwtSignKey}")
  private String jwtSignKey;

  @Value("${tecpie.security.duplicateLogin:true}")
  private boolean duplicateLogin;

  @Autowired
  private SysUserTokenService userTokenService;

  @Autowired(required = false)
  private TecpieLockUtil tecpieLockUtil;

  public static Map<String, Object> convertUser(SysUserDetails sysUser) {
    Map<String, Object> targetMap = new HashMap<>(2);
    Map<String, Object> authcMap = new HashMap<>(2);
    Map<String, String> infoMap = new HashMap<>(4);
    infoMap.put("id", String.valueOf(sysUser.getId()));
    infoMap.put("name", sysUser.getName());
    infoMap.put("code", sysUser.getCode());
    infoMap.put("token", sysUser.getToken());
    infoMap.put("engineersTeamId", ObjectUtils.isEmpty(sysUser.getEngineersTeamId()) ? "" : sysUser.getEngineersTeamId().toString());

    authcMap.put("principal", infoMap);
    Map<String, String> credentials = new HashMap<>(infoMap);
    authcMap.put("credentials", credentials);

    targetMap.put("authc", authcMap);

    Map<String, Object> authzMap = new HashMap<>(10);

    authzMap.put("roles",
        sysUser.getUserGrantedAuthority().stream().map(SysGrantedAuthority::getRoleOrPermission)
            .filter(rop -> rop.startsWith("ROLE")).collect(
                Collectors.toList()));
    authzMap.put("permissions",
        sysUser.getUserGrantedAuthority().stream().map(SysGrantedAuthority::getRoleOrPermission)
            .filter(rop -> !rop.startsWith("ROLE")).collect(
                Collectors.toList()));

    targetMap.put("authz", authzMap);

    Map<String, Object> resultMap = new HashMap<>(2);
    resultMap.put("info", targetMap);
    return resultMap;
  }

  /**
   * 验证失败返回前端数据
   *
   * @param response 响应
   * @param code     错误编码
   * @param message  错误信息
   */
  public static void authFailResponse(HttpServletResponse response, int code, String message) {
    response.setContentType("application/json;charset=utf-8");
    try (PrintWriter out = response.getWriter()) {
      out.append(JSON.toJSONString(Result.build(code, message)));
    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * 生成token组
   *
   * @return token组
   */
  public SecurityToken generateTokenPair(String client, Serializable userId) {
    String token = createToken(userId, tokenExpireTime);
    String refreshToken = createToken(userId, refreshTokenExpireTime);
    if (TecpieCacheUtil.getRedisCacheHelper() != null) {
      String clientKey = Optional.ofNullable(client)
          .map(c -> c.concat(":"))
          .orElse("");
      // 将 token 放入至缓存中
      String cacheKey = clientKey + String
          .format("%s:%s", AuthConstants.CACHE_KEY_OF_TOKEN, userId);
      String cacheRefreshKey = clientKey + String
          .format("%s:%s", AuthConstants.CACHE_KEY_OF_REFRESH_TOKEN, userId);
      if (!duplicateLogin) {
        TecpieCacheUtil.clearJsonCommon(AuthConstants.CACHE_NAME_USER_STARTER + ":" + cacheKey);
        TecpieCacheUtil.clearJsonCommon(
            AuthConstants.CACHE_NAME_USER_STARTER + ":" + cacheRefreshKey);
      }
      TecpieCacheUtil.putJsonCommon(AuthConstants.CACHE_NAME_USER_STARTER,
          cacheKey + ":" + token, autoRefreshLimit, tokenExpireTime + autoRefreshLimit,
          TimeUnit.MINUTES);
      TecpieCacheUtil.putJsonCommon(AuthConstants.CACHE_NAME_USER_STARTER,
          cacheRefreshKey + ":" + refreshToken, refreshTokenExpireTime, refreshTokenExpireTime,
          TimeUnit.MINUTES);
    } else {
      if (!duplicateLogin) {
        userTokenService.lambdaUpdate()
            .eq(SysUserToken::getClient, Optional.ofNullable(client).orElse(StringPool.EMPTY))
            .eq(SysUserToken::getUserId, userId).remove();
      }
      SysUserToken userToken = new SysUserToken();
      userToken.setClient(client);
      userToken.setUserId(userId);
      userToken.setToken(token);
      userToken.setRefreshToken(refreshToken);
      userTokenService.save(userToken);
    }
    SecurityToken securityToken = new SecurityToken();
    securityToken.setToken(token);
    securityToken.setRefreshToken(refreshToken);
    return securityToken;
  }

  public String createToken(Serializable id, long tokenExpireTime) {
    return Jwts.builder()
        //主题 放入用户id - by xuliang
        .setSubject(String.valueOf(id))
        //失效时间
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 60 * 1000))
        //签名算法和密钥
        .signWith(SignatureAlgorithm.HS512, jwtSignKey)
        .compact();
  }

  @Transactional(rollbackFor = Exception.class)
  public String generateNewToken(String client, Serializable userId, String token) {
    String newToken = createToken(userId, tokenExpireTime);
    if (TecpieCacheUtil.getRedisCacheHelper() != null) {
      if (!tecpieLockUtil.noWaitLock(token, 15L)) {
        return null;
      }
      // 将 token 放入至缓存中
      String cacheKey = Optional.ofNullable(client)
          .map(c -> c.concat(":"))
          .orElse("") + String
          .format("%s:%s", AuthConstants.CACHE_KEY_OF_TOKEN, userId);
      //延迟过期token
      TecpieCacheUtil.expireJsonCommon(AuthConstants.CACHE_NAME_USER_STARTER + ":" + cacheKey,
          token, 10L, TimeUnit.SECONDS);
      TecpieCacheUtil.putJsonCommon(AuthConstants.CACHE_NAME_USER_STARTER,
          cacheKey + ":" + newToken, autoRefreshLimit, tokenExpireTime + autoRefreshLimit,
          TimeUnit.MINUTES);
    } else {
      SysUserToken userToken = userTokenService.selectForUpdate(token);
      if (userToken != null) {
        userTokenService.lambdaUpdate().eq(SysUserToken::getToken, token)
            .set(BaseEntity::getStatus, CommonConstants.DISABLE).update();
        userToken.setId(null);
        userToken.setToken(newToken);
        userTokenService.save(userToken);
        ThreadUtil.execAsync(() -> {
          try {
            Thread.sleep(10L * 1000);
            userTokenService.lambdaUpdate().eq(SysUserToken::getToken, token).remove();
          } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
            Thread.currentThread().interrupt();
          }
        });
      } else {
        return null;
      }
    }
    return newToken;
  }

  /**
   * 使用RefreshToken刷新token
   *
   * @param refreshToken
   * @return
   */
  public SecurityToken refreshToken(String client, String refreshToken) {
    String jwtTokenKey = TecpieSecurityUtil.getJwtSignKey();
    String userId;
    try {
      userId = Jwts.parser().setSigningKey(jwtTokenKey).parseClaimsJws(refreshToken)
          .getBody().getSubject();
    } catch (SignatureException | MalformedJwtException e) {
      throw new BadCredentialsException(AuthError.TOKEN_INVALID_ERROR.getMessage());
    } catch (ExpiredJwtException ex) {
      throw new BadCredentialsException(AuthError.TOKEN_EXPIRED_ERROR.getMessage());
    }
    String cacheRefreshKey = Optional.ofNullable(client)
        .map(c -> c.concat(":"))
        .orElse("") + String
        .format("%s:%s:%s", AuthConstants.CACHE_KEY_OF_REFRESH_TOKEN, userId, refreshToken);
    if ((TecpieCacheUtil.getRedisCacheHelper() != null && !TecpieCacheUtil.existJsonCommom(
        AuthConstants.CACHE_NAME_USER_STARTER,
        cacheRefreshKey)) || !userTokenService.lambdaQuery()
        .eq(SysUserToken::getRefreshToken, refreshToken).exists()) {
      throw BusinessException.build(AuthError.TOKEN_EXPIRED_ERROR);
    }
    return generateTokenPair(client, userId);
  }
}
