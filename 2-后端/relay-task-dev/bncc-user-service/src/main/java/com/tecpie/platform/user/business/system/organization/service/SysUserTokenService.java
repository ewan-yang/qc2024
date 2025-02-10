package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.system.organization.entity.SysUserToken;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserTokenMapper;
import com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.query.SysUserTokenQueryCommand;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import java.io.Serializable;
import java.util.List;

/**
 * 用户Token表 服务类接口
 *
 * @author tecpie
 * @since 2023-12-06
 */
public interface SysUserTokenService extends IBaseService<SysUserTokenMapper, SysUserToken> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserToken
   */
  SysUserToken selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserToken>
   */
  List<SysUserToken> searchExtensionPageList(SysUserTokenQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Long
   */
  Serializable saveSysUserToken(SysUserToken entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysUserToken(Serializable id, SysUserToken entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysUserTokenStatus(Serializable id, Integer status);

  boolean checkToken(String token);

  SysUserToken selectForUpdate(String token);
}