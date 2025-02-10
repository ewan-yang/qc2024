package com.tecpie.platform.user.business.system.function.service.impl;

import com.google.common.collect.Lists;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.function.api.command.query.SysPermissionQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import com.tecpie.platform.user.business.system.function.mapper.SysPermissionMapper;
import com.tecpie.platform.user.business.system.function.service.SysPermissionService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统权限表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

  private static final String NON_EXIST_MESSAGE = "系统权限表[%s]不存在";

  @Override
  public SysPermission selectExtensionById(Serializable id) {
    SysPermission entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysPermission> searchExtensionPageList(SysPermissionQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysPermission(SysPermission entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysPermission(Serializable id, SysPermission entity) {
    SysPermission existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void changeSysPermissionStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysPermission::getStatus, status)
        .set(SysPermission::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysPermission::getUpdateDate, LocalDateTime.now())
        .eq(SysPermission::getId, id).update();

    if (!result) {
      throw BusinessException
          .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

  @Override
  public void cascadeUpdate(List<SysPermission> existEntityList,
      List<SysPermission> updateEntityList) {
    Map<String, SysPermission> existEntityMap = existEntityList.stream().collect(
        Collectors.toMap(SysPermission::getPermissionCode, Function.identity()));
    List<SysPermission> addList = Lists.newArrayList();
    List<SysPermission> updateList = Lists.newArrayList();

    for (SysPermission entity : updateEntityList) {
      if (existEntityMap.containsKey(entity.getPermissionCode())) {
        updateList.add(entity);
        existEntityMap.remove(entity.getPermissionCode());
      } else {
        addList.add(entity);
      }
    }

    this.removeByIds(
        existEntityMap.values().stream().map(BaseEntity::getId).collect(Collectors.toList()));
    this.saveBatch(addList);
    this.updateBatchById(updateList);
  }
}