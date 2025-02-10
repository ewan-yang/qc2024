package com.tecpie.platform.user.security.auth.handler;

import com.alibaba.fastjson.JSON;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.constant.AuthConstants;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.platform.user.utils.SecurityUtils;
import com.tecpie.starter.feign.entity.SecurityToken;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import com.tecpie.starter.webmvc.util.I18nUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.ZoneOffset;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 用户登录成功处理 Copyright (C) TECPIE, All rights reserved.
 *
 * @author ZhangDY
 * @date 2020/3/4 23:11
 */
@Slf4j
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  /**
   * 密码有效期，天为单位，默认为180天，为0为不校验密码有效期
   */
  @Value("${tecpie.security.passwordTerm:0}")
  private Integer passwordTerm;

  @Autowired
  private SecurityUtils securityUtils;

  @Autowired
  private SysUserService userService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext()
        .getAuthentication().getDetails();
    log.info("login--IP:" + details.getRemoteAddress());

    Object principal = authentication.getPrincipal();
    response.setContentType("application/json;charset=utf-8");

    try (PrintWriter out = response.getWriter()) {
      if (principal instanceof SysUser) {
        SysUserDetails sysUserDetail = (SysUserDetails) principal;
        Serializable id = sysUserDetail.getId();
        // 生成token
        SecurityToken securityToken = securityUtils.generateTokenPair(
            request.getHeader(AuthConstants.CLIENT_HEADER), id);
        //刷新用户信息缓存
        UserCacheHelper.removeSecurityUser(id.toString());
        UserCacheHelper.setSecurityUser(userService.selectSecurityUser(id));
        sysUserDetail.setToken(securityToken.getToken());
        sysUserDetail.setRefreshToken(securityToken.getRefreshToken());
        Result<Map<String, Object>> result;
        if (passwordTerm != 0 &&
            (System.currentTimeMillis() - sysUserDetail.getPasswordDate()
                .toInstant(ZoneOffset.of("+8")).toEpochMilli())
                > (passwordTerm - 7) * 24 * 60 * 60 * 1000L) {
          result = Result.build(ResultCode.USER_PASSWORD_WILL_EXPIRE.getCode(),
              I18nUtil.getMessage(ResultCode.USER_PASSWORD_WILL_EXPIRE.name()),
              SecurityUtils.convertUser(sysUserDetail));
        } else {
          result = Result.success(SecurityUtils.convertUser(sysUserDetail));
        }
        out.append(JSON.toJSONString(result));
        out.flush();
      }
    } catch (IOException e) {
      // TODO 是否需要抛出异常
      log.error(ExceptionUtils.getStackTrace(e));
    }
  }
}
