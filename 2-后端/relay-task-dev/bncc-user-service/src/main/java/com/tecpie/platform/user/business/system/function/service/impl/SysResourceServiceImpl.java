package com.tecpie.platform.user.business.system.function.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.function.api.command.query.SysResourceQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.entity.SysResource;
import com.tecpie.platform.user.business.system.function.mapper.SysResourceMapper;
import com.tecpie.platform.user.business.system.function.service.SysPermissionService;
import com.tecpie.platform.user.business.system.function.service.SysResourceService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统资源表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceServiceImpl extends
    BaseServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

  private static final String NON_EXIST_MESSAGE = "系统资源表[%s]不存在";

  @Autowired
  private SysPermissionService permissionService;

  @Override
  public SysResource selectExtensionById(Serializable id) {
    SysResource entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException
          .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysResource> searchExtensionPageList(SysResourceQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysResource(SysResource entity) {
    this.save(entity);
    List<SysPermission> permissions = new ArrayList<>();
    for (SysPermission permission : entity.getPermissionList()) {
      permission.setResourceId(entity.getId());
      permissions.add(permission);
    }
    permissionService.saveBatch(permissions);
    return entity.getId();
  }

  @Override
  public void updateSysResource(Serializable id, SysResource entity) {
    SysResource existEntity = this.baseMapper.selectExtensionById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
    permissionService.cascadeUpdate(existEntity.getPermissionList(), entity.getPermissionList());
  }

  @Override
  public void changeSysResourceStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysResource::getStatus, status)
        .set(SysResource::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysResource::getUpdateDate, LocalDateTime.now())
        .eq(SysResource::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

}