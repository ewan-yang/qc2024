package com.tecpie.platform.user.security.auth.provider;

import com.tecpie.platform.user.business.organization.api.enums.LoginFlagEnum;
import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.platform.user.security.auth.service.UserDetailServiceImpl;
import com.tecpie.starter.webmvc.util.I18nUtil;
import io.micrometer.core.instrument.util.StringUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * spring security 自定义鉴权
 *
 * @author huangaoqin
 */
@Slf4j
@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  /**
   * 是否合并验证用户名密码，为true表示用户名和密码合并验证，不会单独给出用户名错误或密码错误的信息
   */
  @Value("${tecpie.security.allCheck:false}")
  private Boolean allCheck;

  /**
   * 密码有效期，天为单位，默认为180天，为0为不校验密码有效期 TODO 需在系统中增加有效期到达后，提示用户修改密码的功能
   */
  @Value("${tecpie.security.passwordTerm:0}")
  private Integer passwordTerm;

  /**
   * 短期锁定时长
   */
  @Value("${tecpie.security.shortLockTime:0}")
  private Integer shortLockTime;

  @Autowired
  private UserDetailServiceImpl userDetailService;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    // 进行附加的安全检查，目前没有，所以为空
  }

  @Override
  protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    // 根据用户名获取用户信息，本方法未实现
    return null;
  }

  /**
   * 自定义认证方法
   * @param authentication the authentication request object.
   *
   * @return 认证信息
   * @throws AuthenticationException 认证异常
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 用户输入的用户名
    String username = authentication.getName();
    // 用户输入的密码
    String password = authentication.getCredentials().toString();
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      log.warn(ResultCode.USER_NAME_PASSWORD_ERROR.getMessage());
      throw new BadCredentialsException(ResultCode.USER_NAME_PASSWORD_ERROR.getMessage());
    }
    // 通过自定义的CustomUserDetailsService，以用户输入的用户名查询用户信息
    SysUserDetails userDetails = (SysUserDetails) userDetailService.loadUserByUsername(username);

    boolean userExist = userDetails != null;
    boolean passwordCorrect = userExist && userDetails.getPassword().equals(password);

    // 用户不存在
    if (!userExist) {
      log.warn(ResultCode.USER_NAME_NON_EXIST.getMessage());
      throw new BadCredentialsException(I18nUtil.getMessage(
          Boolean.TRUE.equals(allCheck) ? ResultCode.USER_NAME_PASSWORD_ERROR.name()
              : ResultCode.USER_NAME_NON_EXIST.name()));
    }
    String remoteAddres = ((WebAuthenticationDetails) authentication.getDetails())
        .getRemoteAddress();
    //长期锁定
    if (LoginFlagEnum.LONG_LOCK == userDetails.getLoginFlag()) {
      log.warn(ResultCode.USER_LONG_LOCK.getMessage());
      throw new BadCredentialsException(I18nUtil.getMessage(ResultCode.USER_LONG_LOCK.name()));
    }
    //登录失败次数已达上限短期锁定
    if (LoginFlagEnum.SHORT_LOCK == userDetails.getLoginFlag()
        && userDetails.getLockDate().plusMinutes(shortLockTime).isAfter(LocalDateTime.now())) {
      log.warn(ResultCode.USER_SHORT_LOCK.getMessage());
      throw new BadCredentialsException(I18nUtil
          .getMessage(ResultCode.USER_SHORT_LOCK.name(),
              new String[]{String.valueOf(
                  shortLockTime - Duration.between(userDetails.getLockDate(), LocalDateTime.now())
                      .toMinutes())}));
    }
    // 校验用户密码
    if (!passwordCorrect) {
      log.warn(ResultCode.USER_PASSWORD_ERROR.getMessage());
      userDetailService.loginFailed(remoteAddres, userDetails);
      throw new BadCredentialsException(I18nUtil.getMessage(
          Boolean.TRUE.equals(allCheck) ? ResultCode.USER_NAME_PASSWORD_ERROR.name()
              : ResultCode.USER_PASSWORD_ERROR.name()));
    }
    if (passwordTerm != 0 &&
        (System.currentTimeMillis() - userDetails.getPasswordDate().toInstant(ZoneOffset.of("+8"))
            .toEpochMilli()) > passwordTerm * 24 * 60 * 60 * 1000L) {
      log.warn(ResultCode.USER_PASSWORD_EXPIRED.getMessage());
      throw new BadCredentialsException(
          I18nUtil.getMessage(ResultCode.USER_PASSWORD_EXPIRED.name()));
    }
    //登录成功记录
    userDetailService.loginSuccess(remoteAddres, userDetails);
    //将用户信息塞到SecurityContext中，方便获取当前用户信息
    return this.createSuccessAuthentication(userDetails, authentication, userDetails);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
