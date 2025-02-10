package com.tecpie.platform.user.security.auth.handler;

import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.utils.SecurityUtils;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 用户验证错误处理类 Copyright (C) TECPIE, All rights reserved.
 *
 * @author ZhangDY
 * @date 2020/3/4 23:09
 */
@Slf4j
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    Arrays.stream(ResultCode.values())
        .filter(resultCode -> resultCode.getMessage().equals(e.getMessage())).findFirst()
        .ifPresentOrElse(resultCode -> SecurityUtils.authFailResponse(response, resultCode.getCode(), e.getMessage()),
                () -> SecurityUtils.authFailResponse(response, ResultCode.LOGIN_FAIL_CODE.getCode(), e.getMessage()));
  }
}
