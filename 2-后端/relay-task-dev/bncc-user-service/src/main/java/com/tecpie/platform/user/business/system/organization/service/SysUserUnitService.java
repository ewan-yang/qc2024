package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysUserUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserUnitSaveCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserUnitMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户组织关系表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysUserUnitService extends IBaseService<SysUserUnitMapper, SysUserUnit> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserUnit
   */
  SysUserUnit selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserUnit>
   */
  List<SysUserUnit> searchExtensionPageList(SysUserUnitQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysUserUnit(SysUserUnit entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysUserUnit(Serializable id, SysUserUnit entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysUserUnitStatus(Serializable id, Integer status);

  boolean saveOrUpdateBatch(List<SysUserUnitSaveCommand> commands);
}