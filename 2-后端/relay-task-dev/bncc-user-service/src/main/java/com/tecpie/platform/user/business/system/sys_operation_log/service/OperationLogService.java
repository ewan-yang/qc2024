package com.tecpie.platform.user.business.system.sys_operation_log.service;

import com.tecpie.platform.user.business.system.sys_operation_log.entity.OperationLog;
import com.tecpie.platform.user.business.system.sys_operation_log.mapper.OperationLogMapper;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogQueryCommand;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志表 服务类接口
 *
 * @author tecpie
 * @since 2023-12-09
 */
public interface OperationLogService extends IBaseService<OperationLogMapper, OperationLog> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return OperationLog
   */
  OperationLog selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<OperationLog>
   */
  List<OperationLog> searchExtensionPageList(OperationLogQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveOperationLog(OperationLog entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateOperationLog(Serializable id, OperationLog entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeOperationLogStatus(Serializable id, Integer status);

}