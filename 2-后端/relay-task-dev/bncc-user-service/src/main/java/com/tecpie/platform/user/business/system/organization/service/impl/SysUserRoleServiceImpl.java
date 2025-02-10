package com.tecpie.platform.user.business.system.organization.service.impl;

import com.google.common.collect.Lists;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserRoleQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserRoleSaveCommand;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUserRoleSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserRoleMapper;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色关联表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

  private static final String NON_EXIST_MESSAGE = "用户角色关联表[%s]不存在";

  @Override
  public SysUserRole selectExtensionById(Serializable id) {
    SysUserRole entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysUserRole> searchExtensionPageList(SysUserRoleQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysUserRole(SysUserRole entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysUserRole(Serializable id, SysUserRole entity) {
    SysUserRole existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void cascadeUpdate(List<SysUserRole> existEntityList, List<SysUserRole> updateEntityList) {
    Map<String, SysUserRole> existEntityMap = existEntityList.stream().collect(Collectors.toMap(sysUserRole -> String.format("%d,%d", sysUserRole.getUserId(), sysUserRole.getRoleId()), Function.identity()));
    Map<String, SysUserRole> updateEntityMap = updateEntityList.stream().collect(Collectors.toMap(sysUserRole -> String.format("%d,%d", sysUserRole.getUserId(), sysUserRole.getRoleId()), Function.identity()));

    List<SysUserRole> addList = Lists.newArrayList();
    List<SysUserRole> removeList = Lists.newArrayList();

    Iterator<SysUserRole> existEntityIter = existEntityList.iterator();
    Iterator<SysUserRole> updateEntityIter = updateEntityList.iterator();

    // 根据待更新的实体列表，找出需要新增的实体
    // 由于是userId、roleId为联合主键，对于UserRole来说不需要更新，只有添加和删除操作
    while(updateEntityIter.hasNext()) {
      SysUserRole updateEntity = updateEntityIter.next();
      String updateEntityKey = String.format("%d,%d", updateEntity.getUserId(), updateEntity.getRoleId());
      if (!existEntityMap.containsKey(updateEntityKey)) {
        addList.add(updateEntity);
      }
    }

    // 根据已经存在的实体，找出需要删除的实体
    while (existEntityIter.hasNext()) {
      SysUserRole existEntity = existEntityIter.next();
      String existEntityKey = String.format("%d,%d", existEntity.getUserId(), existEntity.getRoleId());
      if (!updateEntityMap.containsKey(existEntityKey)) {
        removeList.add(existEntity);
      }
    }

    for (SysUserRole removeUserRole : removeList) {
      Map<String, Object> removeConditionMap = new HashMap<>();
      removeConditionMap.put("user_id", removeUserRole.getUserId());
      removeConditionMap.put("role_id", removeUserRole.getRoleId());
      this.removeByMap(removeConditionMap);
    }
    this.saveBatch(addList);
  }

  @Override
  public void changeSysUserRoleStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysUserRole::getStatus, status)
        .set(SysUserRole::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysUserRole::getUpdateDate, LocalDateTime.now())
        .eq(SysUserRole::getId, id).update();

    if (!result) {
      throw BusinessException
          .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

  @Override
  public boolean saveOrUpdateBatch(List<SysUserRoleSaveCommand> commands) {
    Set<Serializable> userIds = new HashSet<>();
    Set<Serializable> roleIds = new HashSet<>();
    for (SysUserRoleSaveCommand saveCommand : commands) {
      userIds.add(saveCommand.getUserId());
      roleIds.add(saveCommand.getRoleId());
    }
    //存在不同用户，即角色修改用户关系
    if (userIds.size() > 1) {
      this.lambdaUpdate().in(SysUserRole::getRoleId, roleIds).remove();
      return this.saveBatch(SysUserRoleSaveCommandAssembler.INSTANCE.parseList(commands));
    } else {
      //不存在不同用户，即用户修改角色关系
      this.lambdaUpdate().in(SysUserRole::getUserId, userIds).remove();
      return this.saveBatch(SysUserRoleSaveCommandAssembler.INSTANCE.parseList(commands));
    }
  }
}