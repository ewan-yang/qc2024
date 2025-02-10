package com.tecpie.platform.user.business.organization.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tecpie.library.common.util.enumerable.IEnumerable;
import lombok.Getter;

/**
 * 用户登录控制
 *
 * @author aoqin.huang
 * @version v1.0.0
 * @date 2022/12/05
 */
@Getter
public enum LoginFlagEnum implements IEnumerable {

  //长期锁定
  LONG_LOCK(0, "长期锁定"),
  //可登录
  LOGIN_ABLE(1, "可登录"),
  //短期锁定
  SHORT_LOCK(2, "短期锁定");

  @EnumValue
  @JsonValue
  private final Integer code;

  private final String value;

  LoginFlagEnum(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

}
