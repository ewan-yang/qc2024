package com.tecpie.platform.user.business.system.function.service;

import com.tecpie.platform.user.business.function.api.command.query.SysOperationQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysOperation;
import com.tecpie.platform.user.business.system.function.mapper.SysOperationMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 操作信息表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
public interface SysOperationService extends IBaseService<SysOperationMapper, SysOperation> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysOperation
   */
  SysOperation selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysOperation>
   */
  List<SysOperation> searchExtensionPageList(SysOperationQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysOperation(SysOperation entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysOperation(Serializable id, SysOperation entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysOperationStatus(Serializable id, Integer status);

}