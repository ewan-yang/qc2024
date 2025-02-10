package com.tecpie.platform.user.security.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户手机号验证请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户手机号验证请求参数")
@Getter
@Setter
public class UserMoibleVerifyCommand {

  /**
   * 密码
   */
  @Schema(description = "密码")
  private String password;

  /**
   * 手机号码
   */
  @Schema(description = "手机号码")
  private String mobile;

  @Schema(description = "验证码")
  private String verifyCode;
}