package com.tecpie.platform.user.business.system.function.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.function.api.command.query.SysOperationQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysOperation;
import com.tecpie.platform.user.business.system.function.mapper.SysOperationMapper;
import com.tecpie.platform.user.business.system.function.service.SysOperationService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作信息表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysOperationServiceImpl extends BaseServiceImpl<SysOperationMapper, SysOperation> implements SysOperationService {

  private static final String NON_EXIST_MESSAGE = "操作信息表[%s]不存在";

  @Override
  public SysOperation selectExtensionById(Serializable id) {
    SysOperation entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysOperation> searchExtensionPageList(SysOperationQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysOperation(SysOperation entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysOperation(Serializable id, SysOperation entity) {
    SysOperation existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void changeSysOperationStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysOperation::getStatus, status)
        .set(SysOperation::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysOperation::getUpdateDate, LocalDateTime.now())
        .eq(SysOperation::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

}