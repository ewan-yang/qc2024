package com.tecpie.platform.user.security.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用户被授予的权限
 *
 * @author tecpie
 */
@Data
public class SysGrantedAuthority implements GrantedAuthority {

  private final String roleOrPermission;

  @Override
  public String getAuthority() {
    return this.roleOrPermission;
  }
}
