package com.tecpie.platform.user.constants;

import org.springframework.context.annotation.Configuration;

/**
 * 常量定义类
 *
 * @author tecpie
 */
@Configuration
public class SecurityConstant {

  /**
   * User Starter 中缓存在 CacheManager 或 Redis 中的缓存名称
   */
  public static final String CACHE_NAME_USER_STARTER = "bncc-user-starter:cache";
  public static final String CACHE_KEY_OF_KAPTCHA = "kaptcha";
  public static final String CACHE_KEY_OF_TOKEN = "token";
  public static final String CACHE_KEY_OF_MSG_VERIFY_CODE = "msg_verify_code";
  public static final String CACHE_KEY_OF_PWD_VERIFY_CODE = "pwd_verify_code";
  public static final String JWT_SIGN_KEY = "TecpieBaseSecurity2021!@#";

}
