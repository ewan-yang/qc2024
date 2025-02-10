package com.tecpie.platform.user.security.auth.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 存储验证码
 *
 * @author tecpie
 */
@Getter
@Setter
public class ValidateCode implements Serializable {

  private static final long serialVersionUID = -6261442474822445099L;

  private String code;
  private LocalDateTime expireTime;

  /**
   * TODO 检查该方法是否需要
   * @return 是否过期
   */
  public boolean isExpried() {
    return LocalDateTime.now().isAfter(expireTime);
  }

  public ValidateCode(String code) {
    this.code = code;
    this.expireTime = LocalDateTime.now().plusSeconds(60000);
  }
}
