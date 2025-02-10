package com.tecpie.platform.user.business.system.function.service;

import com.tecpie.platform.user.business.function.api.command.query.SysResourceQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysResource;
import com.tecpie.platform.user.business.system.function.mapper.SysResourceMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 系统资源表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
public interface SysResourceService extends IBaseService<SysResourceMapper, SysResource> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysResource
   */
  SysResource selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysResource>
   */
  List<SysResource> searchExtensionPageList(SysResourceQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysResource(SysResource entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysResource(Serializable id, SysResource entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysResourceStatus(Serializable id, Integer status);

}