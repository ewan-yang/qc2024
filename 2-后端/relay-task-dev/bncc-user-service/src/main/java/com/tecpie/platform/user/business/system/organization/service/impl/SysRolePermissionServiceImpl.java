package com.tecpie.platform.user.business.system.organization.service.impl;

import com.google.common.collect.Lists;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysRolePermissionQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import com.tecpie.platform.user.business.system.organization.mapper.SysRolePermissionMapper;
import com.tecpie.platform.user.business.system.organization.service.SysRolePermissionService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色与系统权限关联表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

  private static final String NON_EXIST_MESSAGE = "角色与系统权限关联表[%s]不存在";

  @Override
  public SysRolePermission selectExtensionById(Serializable id) {
    SysRolePermission entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysRolePermission> searchExtensionPageList(SysRolePermissionQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysRolePermission(SysRolePermission entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysRolePermission(Serializable id, SysRolePermission entity) {
    SysRolePermission existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void cascadeUpdate(List<SysRolePermission> existEntityList, List<SysRolePermission> updateEntityList) {
    Map<String, SysRolePermission> existEntityMap = existEntityList.stream().collect(Collectors.toMap(sysRolePermission -> String.format("%d,%d", sysRolePermission.getRoleId(), sysRolePermission.getPermissionId()), Function.identity()));
    Map<String, SysRolePermission> updateEntityMap = updateEntityList.stream().collect(Collectors.toMap(sysRolePermission -> String.format("%d,%d", sysRolePermission.getRoleId(), sysRolePermission.getPermissionId()), Function.identity()));

    List<SysRolePermission> addList = Lists.newArrayList();
    List<SysRolePermission> removeList = Lists.newArrayList();

    Iterator<SysRolePermission> existEntityIter = existEntityList.iterator();
    Iterator<SysRolePermission> updateEntityIter = updateEntityList.iterator();

    // 根据待更新的实体列表，找出需要新增的实体
    // 由于是roleId、permissionId为联合主键，对于UserPermission来说不需要更新，只有添加和删除操作
    while(updateEntityIter.hasNext()) {
      SysRolePermission updateEntity = updateEntityIter.next();
      String updateEntityKey = String.format("%d,%d", updateEntity.getRoleId(), updateEntity.getPermissionId());
      if (!existEntityMap.containsKey(updateEntityKey)) {
        addList.add(updateEntity);
      }
    }

    // 根据已经存在的实体，找出需要删除的实体
    while (existEntityIter.hasNext()) {
      SysRolePermission existEntity = existEntityIter.next();
      String existEntityKey = String.format("%d,%d", existEntity.getRoleId(), existEntity.getPermissionId());
      if (!updateEntityMap.containsKey(existEntityKey)) {
        removeList.add(existEntity);
      }
    }

    for (SysRolePermission removeRolePermission : removeList) {
      Map<String, Object> removeConditionMap = new HashMap<>();
      removeConditionMap.put("role_id", removeRolePermission.getRoleId());
      removeConditionMap.put("permission_id", removeRolePermission.getPermissionId());
      this.removeByMap(removeConditionMap);
    }
    this.saveBatch(addList);
  }

  @Override
  public void changeSysRolePermissionStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysRolePermission::getStatus, status)
        .set(SysRolePermission::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysRolePermission::getUpdateDate, LocalDateTime.now())
        .eq(SysRolePermission::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

}