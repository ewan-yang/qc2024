package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUnitResource;
import com.tecpie.platform.user.business.system.organization.entity.SysUnit;
import com.tecpie.platform.user.business.system.organization.mapper.SysUnitMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 组织结构表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysUnitService extends IBaseService<SysUnitMapper, SysUnit> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUnit
   */
  SysUnit selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUnit>
   */
  List<SysUnitResource> searchExtensionPageList(SysUnitQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysUnit(SysUnit entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysUnit(Serializable id, SysUnit entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysUnitStatus(Serializable id, Integer status);

  /**
   * 根据用户id获取用户单位信息集合
   *
   * @param userIdList 用户标识列表
   * @return {@link List}<{@link SysUnit}>
   */
  List<SysUnit> listByUserIdList(List<Serializable> userIdList);

}