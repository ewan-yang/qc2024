package com.tecpie.platform.user.security.auth.service;

import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.user.business.organization.api.enums.LoginFlagEnum;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

  /**
   * 最大登录失败次数
   */
  @Value("${tecpie.security.maxFailCount:0}")
  private Integer maxFailCount;

  @Autowired
  private SysUserService sysUserService;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    SysUser sysUser = sysUserService.selectAuthByCode(userName);
    if (sysUser != null) {
      return new SysUserDetails(sysUser);
    } else {
      return null;
    }
  }

  /**
   * 登录失败
   *
   * @param remoteAddress
   * @param sysUser
   * @return
   */
  public boolean loginFailed(String remoteAddress, SysUser sysUser) {
    if (maxFailCount != 0) {
      Integer failCount = Optional.ofNullable(sysUser.getLoginFailCount()).orElse(0) + 1;
      return sysUserService.lambdaUpdate().eq(BaseEntity::getId, sysUser.getId())
          .set(SysUser::getLoginFailDate,
              LocalDateTime.now()).set(SysUser::getLoginFailCount, failCount)
          .set(SysUser::getLoginFailIp, remoteAddress)
          .set(failCount >= maxFailCount, SysUser::getLoginFlag,
              LoginFlagEnum.SHORT_LOCK).set(SysUser::getLockDate, LocalDateTime.now()).update();
    } else {
      return false;
    }
  }

  /**
   * 登录成功
   *
   * @param remoteAddress
   * @param sysUser
   * @return
   */
  public boolean loginSuccess(String remoteAddress, SysUser sysUser) {
    return sysUserService.lambdaUpdate().eq(BaseEntity::getId, sysUser.getId())
        .set(SysUser::getLoginSuccessDate,
            LocalDateTime.now()).set(SysUser::getLoginFailCount, 0)
        .set(SysUser::getLoginFlag, LoginFlagEnum.LOGIN_ABLE)
        .set(SysUser::getLoginSuccessIp, remoteAddress).update();
  }
}
