package com.tecpie.platform.user.security.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.constant.CommonConstants;
import com.tecpie.library.common.util.cache.TecpieCacheUtil;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.constants.SecurityConstant;
import com.tecpie.platform.user.security.auth.handler.CustomizeAuthenticationFailureHandler;
import com.tecpie.platform.user.security.auth.handler.CustomizeAuthenticationSuccessHandler;
import com.tecpie.platform.user.security.auth.model.ValidateCode;
import com.tecpie.starter.webmvc.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 继承自UsernamePasswordAuthenticationFilter 登录时的参数获取方式从url中获取改为从请求体中获取
 * rights reserved.
 *
 * @author ZhangDY
 * @since 2020/3/4 23:05
 */
@Slf4j
@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  @Value("${tecpie.security.kaptcha.length:0}")
  private Integer kaptchaLength;

  @Value("${tecpie.security.msgVerify.length:0}")
  private Integer msgVerifyCodeLength;

  @Autowired
  private SysUserService sysUserService;

  protected LoginAuthenticationFilter(@Value("${tecpie.security.loginUrl}") String loginUrl) {
    super(loginUrl);
  }

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;
  @Autowired
  private CustomizeAuthenticationFailureHandler authenticationFailureHandler;

  @PostConstruct
  public void postConstruct() {
    this.setAuthenticationManager(authenticationManager);
    this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    this.setAuthenticationFailureHandler(authenticationFailureHandler);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try (InputStream is = request.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> requestParams = mapper.readValue(is, Map.class);
      //手机号短信登录
      String mobile = requestParams.get("mobile");
      if (msgVerifyCodeLength != 0 && !StringUtils.isEmpty(mobile)) {
        String verifyCode = requestParams.get("msgVerifyCode");
        validateMsgVerifyCode(mobile, verifyCode);
        List<SysUser> sysUsers = sysUserService.lambdaQuery()
            .eq(BaseEntity::getDeleted, CommonConstants.DELETED_FALSE).and(
                wrapper -> wrapper.eq(SysUser::getMobile, mobile).or().eq(SysUser::getCode, mobile))
            .list();
        if (sysUsers.isEmpty()) {
          log.warn(ResultCode.MOBILE_NOT_EXIST.getMessage());
          throw new BadCredentialsException(
              I18nUtil.getMessage(ResultCode.MOBILE_NOT_EXIST.name()));
        }
        SysUser sysUser = sysUsers.get(0);
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            sysUser.getCode(), sysUser.getPassword());
        authentication.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return getAuthenticationManager().authenticate(authentication);
      } else {
        // 读取并校验校验验
        if (kaptchaLength != 0) {
          String verifyCode = requestParams.get("verifyCode");
          validateVerifyCode(request, verifyCode);
        }
        // 读取用户名密码并构造鉴权请求
        String username = requestParams.get("name");
        String password = requestParams.get("password");

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            username, password);
        authentication.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return getAuthenticationManager().authenticate(authentication);
      }
    } catch (IOException e) {
      Authentication authentication = new UsernamePasswordAuthenticationToken("", "");
      return getAuthenticationManager().authenticate(authentication);
    }
  }

  /**
   * 验证用户输入的验证码
   * @param inputVerifyCode 用户输入的验证码
   * @return 验证码是否正确
   */
  private void validateVerifyCode(HttpServletRequest request, String inputVerifyCode) {
    // 校验验证码是否不存在或者已经过期
    String cacheKey = String.format("%s:%s", SecurityConstant.CACHE_KEY_OF_KAPTCHA,
        Base64Utils.encodeToString(request.getRemoteAddr().getBytes()));
    ValidateCode verifyCode = (ValidateCode) TecpieCacheUtil
        .get(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
    if (verifyCode == null || !verifyCode.getExpireTime().isAfter(LocalDateTime.now())) {
      log.warn(ResultCode.KAPTCHA_EXPIRED.getMessage());
      throw new DisabledException(I18nUtil.getMessage(ResultCode.KAPTCHA_EXPIRED.name()));
    }

    // 校验验证码是否正确
    if (inputVerifyCode == null || !verifyCode.getCode()
        .equalsIgnoreCase(inputVerifyCode.toLowerCase())) {
      log.warn(ResultCode.KAPTCHA_ERROR.getMessage());
      throw new DisabledException(I18nUtil.getMessage(ResultCode.KAPTCHA_ERROR.name()));
    }
  }

  /**
   * 验证用户输入的验证码
   *
   * @param inputVerifyCode 用户输入的验证码
   * @return 验证码是否正确
   */
  private void validateMsgVerifyCode(String mobile, String inputVerifyCode) {
    // 校验验证码是否不存在或者已经过期
    String cacheKey = String.format("%s:%s", SecurityConstant.CACHE_KEY_OF_MSG_VERIFY_CODE, mobile);
    String verifyCode = (String) TecpieCacheUtil
        .get(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
    if (verifyCode == null) {
      log.warn(ResultCode.KAPTCHA_EXPIRED.getMessage());
      throw new DisabledException(I18nUtil.getMessage(ResultCode.KAPTCHA_EXPIRED.name()));
    }

    // 校验验证码是否正确
    if (!verifyCode.equals(inputVerifyCode)) {
      log.warn(ResultCode.KAPTCHA_ERROR.getMessage());
      throw new DisabledException(I18nUtil.getMessage(ResultCode.KAPTCHA_ERROR.name()));
    }
    TecpieCacheUtil.remove(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
  }
}
