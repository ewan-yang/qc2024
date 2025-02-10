package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysUserRoleQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserRoleSaveCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserRoleMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色关联表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysUserRoleService extends IBaseService<SysUserRoleMapper, SysUserRole> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserRole
   */
  SysUserRole selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserRole>
   */
  List<SysUserRole> searchExtensionPageList(SysUserRoleQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysUserRole(SysUserRole entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysUserRole(Serializable id, SysUserRole entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysUserRoleStatus(Serializable id, Integer status);

  boolean saveOrUpdateBatch(List<SysUserRoleSaveCommand> commands);
}