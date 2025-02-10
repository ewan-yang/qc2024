package com.tecpie.platform.user.constants;

import com.tecpie.library.common.exception.IEnumerableError;
import com.tecpie.starter.webmvc.util.I18nUtil;

/**
 * 响应状态码
 * TODO 状态码的范围需要确认，SESSION_TIMEOUT、FORCE_LOGOUT、LIMIT_LOGOUT在AccessController中是否还需要
 *
 * @author tecpie
 */
public enum ResultCode implements IEnumerableError {

  USER_PASSWORD_WILL_EXPIRE(4001, "您的密码即将过期，请及时更新密码"),
  USER_PASSWORD_EXPIRED(4002, "您的密码已经过期，请更新密码"),
  USER_NAME_PASSWORD_ERROR(4003, "用户名/密码错误"),
  USER_NAME_NON_EXIST(4004, "用户名不存在"),
  USER_PASSWORD_ERROR(4005, "密码错误"),
  KAPTCHA_EXPIRED(4006, "验证码过期，请重新验证"),
  KAPTCHA_ERROR(4007, "验证码输入错误，请重新验证"),
  SESSION_TIMEOUT(4010, "会话已失效，请重新登录!"),
  FORCE_LOGOUT(4011, "您已被管理员强制下线!"),
  LIMIT_LOGOUT(4012, "您的账户已在其他地方登陆,会话已失效!"),
  USER_SHORT_LOCK(4013, "您的账号已被锁定，剩余锁定时间{0}分钟"),
  USER_LONG_LOCK(4014, "您的账号已被管理员锁定，请联系管理员解锁"),
  MOBILE_NOT_EXIST(4015, "手机号未注册"),
  VERIFY_CODE_SENDED(4016, "验证码已发送，请勿重复获取"),
  ACCOUNT_HAS_REGISTERED(4017, "用户已注册"),
  SSO_FAIL(6111,"单点登录失败"),
  /**
   * 该错误码为不符合上述错误码时返回，其message内容实际为exception.getMessage()
   */
  LOGIN_FAIL_CODE(4020, "");

  ResultCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  private final Integer code;

  private final String message;

  @Override
  public String getMessage() {
    return I18nUtil.getMessage(this.name());
  }

  @Override
  public Integer getCode() {
    return this.code;
  }
}
