package com.tecpie.platform.user.business.system.organization.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.system.organization.entity.SysUserToken;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserTokenMapper;
import com.tecpie.platform.user.business.system.organization.service.SysUserTokenService;
import com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.query.SysUserTokenQueryCommand;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Token表 服务类实现
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserTokenServiceImpl extends
    BaseServiceImpl<SysUserTokenMapper, SysUserToken> implements SysUserTokenService {

  /**
   * Token失效时间，单位为分钟，默认为30分钟
   */
  @Value("${tecpie.security.tokenExpireTime:30}")
  private Long tokenExpireTime;

  /**
   * Token自动续期时间，单位为分钟，默认为30分钟
   */
  @Value("${tecpie.security.autoRefreshLimit:30}")
  private Long autoRefreshLimit;

  @Override
  public SysUserToken selectExtensionById(Serializable id) {
    SysUserToken entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR,
          String.format("用户Token表[%s]不存在", id));
    }
    return entity;
  }

  @Override
  public List<SysUserToken> searchExtensionPageList(SysUserTokenQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysUserToken(SysUserToken entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysUserToken(Serializable id, SysUserToken entity) {
    SysUserToken existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR,
          String.format("用户Token表[%s]不存在", id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void changeSysUserTokenStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysUserToken::getStatus, status)
        .set(SysUserToken::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysUserToken::getUpdateDate, LocalDateTime.now())
        .eq(SysUserToken::getId, id).update();

    if (!result) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR,
          String.format("用户Token表[%s]不存在", id));
    }
  }

  @Override
  public boolean checkToken(String token) {
    SysUserToken userToken = this.lambdaQuery().eq(SysUserToken::getToken, token).one();
    if (userToken != null) {
      if (userToken.getCreateDate().plusSeconds(tokenExpireTime + autoRefreshLimit)
          .isBefore(LocalDateTime.now())) {
        this.removeById(userToken.getId());
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  @Override
  public SysUserToken selectForUpdate(String token) {
    return this.baseMapper.selectForUpdate(token);
  }
}