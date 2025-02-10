package com.tecpie.platform.user.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置类
 *
 * @author huangaoqin
 * @date 2019/11/27
 */
@Configuration
public class KaptchaConfig {

  @Value("${tecpie.security.kaptcha.length:0}")
  private String kaptchaLength;

  @Bean(name = "captchaProducer")
  public DefaultKaptcha getKaptchaBean() {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Properties properties = new Properties();
    properties.setProperty("kaptcha.border", "no");
    properties.setProperty("kaptcha.image.width", "130");
    properties.setProperty("kaptcha.image.height", "46");
    if (NumberUtils.isCreatable(kaptchaLength) && Integer.parseInt(kaptchaLength) > 0) {
      properties.setProperty("kaptcha.textproducer.char.length", kaptchaLength);
      properties.setProperty("kaptcha.textproducer.font.color", "black");
    } else {
      properties.setProperty("kaptcha.textproducer.char.length", "1");
      properties.setProperty("kaptcha.textproducer.font.color", "238,238,238");
      properties.setProperty("kaptcha.textproducer.font.size", "1");
    }
    properties.setProperty("kaptcha.textproducer.char.string", "2345678ABCDEFGHJKLMNPQRSTUVWXYZ");
    Config config = new Config(properties);
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }
}
