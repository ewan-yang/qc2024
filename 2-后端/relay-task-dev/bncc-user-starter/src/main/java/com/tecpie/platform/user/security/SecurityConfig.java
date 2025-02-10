package com.tecpie.platform.user.security;

import com.tecpie.platform.user.security.auth.filter.LoginAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security 主配置类 Copyright (C) TECPIE, All rights reserved.
 *
 * @author huangaoqin
 * @date 2019/11/27
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${tecpie.security.cors:*}")
  private String cors;

  @Value("${tecpie.security.single:false}")
  public boolean single;

  @Autowired
  private LoginAuthenticationFilter loginAuthenticationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (single) {
      // 开启跨域访问
      http.cors().configurationSource(corsConfigurationSource());
    }
    // 关闭 csrf 校验
    http.csrf().disable();
    // 关闭 session 管理
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 禁止浏览器缓存
    http.headers().cacheControl();

    // 使用 loginAuthenticationFilter 替换原有的 usernamePasswordAuthenticationFilter
    http.addFilterAt(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.addAllowedOrigin(cors);
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    return request -> configuration;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
