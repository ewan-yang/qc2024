package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysRolePermissionQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import com.tecpie.platform.user.business.system.organization.mapper.SysRolePermissionMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 角色与系统权限关联表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysRolePermissionService extends IBaseService<SysRolePermissionMapper, SysRolePermission> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysRolePermission
   */
  SysRolePermission selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysRolePermission>
   */
  List<SysRolePermission> searchExtensionPageList(SysRolePermissionQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysRolePermission(SysRolePermission entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysRolePermission(Serializable id, SysRolePermission entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysRolePermissionStatus(Serializable id, Integer status);

}