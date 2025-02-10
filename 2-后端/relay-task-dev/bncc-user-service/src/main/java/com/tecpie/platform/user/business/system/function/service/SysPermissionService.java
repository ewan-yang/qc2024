package com.tecpie.platform.user.business.system.function.service;

import com.tecpie.platform.user.business.function.api.command.query.SysPermissionQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.mapper.SysPermissionMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 系统权限表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
public interface SysPermissionService extends IBaseService<SysPermissionMapper, SysPermission> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysPermission
   */
  SysPermission selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysPermission>
   */
  List<SysPermission> searchExtensionPageList(SysPermissionQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysPermission(SysPermission entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysPermission(Serializable id, SysPermission entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysPermissionStatus(Serializable id, Integer status);

}