package com.tecpie.platform.user.business.system.sys_operation_log.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tecpie.platform.user.business.system.sys_operation_log.entity.OperationLog;
import com.tecpie.platform.user.business.system.sys_operation_log.mapper.OperationLogMapper;
import com.tecpie.platform.user.business.system.sys_operation_log.service.OperationLogService;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogQueryCommand;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志表 服务类实现
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

  @Override
  public OperationLog selectExtensionById(Serializable id) {
    OperationLog entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("操作日志表[%s]不存在", id));
    return entity;
  }

  @Override
  public List<OperationLog> searchExtensionPageList(OperationLogQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveOperationLog(OperationLog entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateOperationLog(Serializable id, OperationLog entity) {
    OperationLog existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("操作日志表[%s]不存在", id));

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void changeOperationLogStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(OperationLog::getStatus, status)
        .set(OperationLog::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(OperationLog::getUpdateDate, LocalDateTime.now())
        .eq(OperationLog::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("操作日志表[%s]不存在", id));
    }
  }

}