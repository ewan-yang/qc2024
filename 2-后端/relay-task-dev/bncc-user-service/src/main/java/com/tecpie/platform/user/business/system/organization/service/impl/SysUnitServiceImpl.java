package com.tecpie.platform.user.business.system.organization.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUnitResource;
import com.tecpie.platform.user.business.system.organization.entity.SysUnit;
import com.tecpie.platform.user.business.system.organization.mapper.SysUnitMapper;
import com.tecpie.platform.user.business.system.organization.service.SysUnitService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织结构表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUnitServiceImpl extends BaseServiceImpl<SysUnitMapper, SysUnit> implements SysUnitService {

  private static final String NON_EXIST_MESSAGE = "组织结构表[%s]不存在";

  @Override
  public SysUnit selectExtensionById(Serializable id) {
    SysUnit entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysUnitResource> searchExtensionPageList(SysUnitQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysUnit(SysUnit entity) {
    this.save(entity);
    this.updateSysUnit(entity.getId(), entity);
    return entity.getId();
  }

  @Override
  public void updateSysUnit(Serializable id, SysUnit entity) {
    SysUnit existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException
          .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    if (StringUtils.isEmpty(entity.getRelationPath())) {
      entity.setRelationPath(entity.getId().toString());
    } else {
      entity
          .setRelationPath(entity.getRelationPath().concat(",").concat(entity.getId().toString()));
    }
    this.updateById(entity);
  }

  @Override
  public void changeSysUnitStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysUnit::getStatus, status)
        .set(SysUnit::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysUnit::getUpdateDate, LocalDateTime.now())
        .eq(SysUnit::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

  @Override
  public List<SysUnit> listByUserIdList(List<Serializable> userIdList) {
    return baseMapper.listByUserIdList(userIdList);
  }

}