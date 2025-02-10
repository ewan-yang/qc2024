package com.tecpie.platform.user.security.auth.model;

import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户信息
 *
 * @author tecpie
 */
@Getter
@Setter
public class SysUserDetails extends SysUser implements UserDetails {

  private static final long serialVersionUID = 43248129131797817L;

  private List<SysGrantedAuthority> userGrantedAuthority;

  private String token;

  private String refreshToken;

  /**
   * 构造函数 TODO 需要将 SysUser 中用户是否过期，是否锁定的字段同步赋值给 SysUserDetail，并需要在相应的方法中返回
   *
   * @param sysUser 用户信息
   */
  public SysUserDetails(SysUser sysUser) {
    if (sysUser != null) {
      this.setId(sysUser.getId());
      this.setName(sysUser.getName());
      this.setCode(sysUser.getCode());
      this.setPassword(sysUser.getPassword());
      this.setPasswordDate(sysUser.getPasswordDate());
        this.setEngineersTeamId(sysUser.getEngineersTeamId());
      this.setLoginFailCount(sysUser.getLoginFailCount());
      this.setLoginFlag(sysUser.getLoginFlag());
      this.setLockDate(sysUser.getLockDate());
      this.setUserGrantedAuthority(sysUser.getRoleList(), sysUser.getPermissionList());
    }
  }

  private void setUserGrantedAuthority(List<SysRole> roleList, List<SysPermission> permissionList) {
    List<SysGrantedAuthority> sysUserGrantedAuthorities = new ArrayList<>();
    roleList.forEach(role -> sysUserGrantedAuthorities.add(new SysGrantedAuthority("ROLE_" + role.getCode())));
    permissionList.forEach(permission -> sysUserGrantedAuthorities.add(new SysGrantedAuthority(permission.getPermissionCode())));
    this.userGrantedAuthority = sysUserGrantedAuthorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return userGrantedAuthority;
  }

  @Override
  public String getUsername() {
    return this.getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public boolean isPermitted(String permission) {
    return this.getAuthorities().stream().anyMatch(
        auth -> auth.getAuthority().equals(permission)
    );
  }
}
