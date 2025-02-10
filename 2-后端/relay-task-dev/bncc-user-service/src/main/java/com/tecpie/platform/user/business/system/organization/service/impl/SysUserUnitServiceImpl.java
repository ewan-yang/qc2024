package com.tecpie.platform.user.business.system.organization.service.impl;

import com.google.common.collect.Lists;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.save.SysUserUnitSaveCommand;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.save.SysUserUnitSaveCommandAssembler;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserUnitMapper;
import com.tecpie.platform.user.business.system.organization.service.SysUserUnitService;
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
 * 用户组织关系表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserUnitServiceImpl extends BaseServiceImpl<SysUserUnitMapper, SysUserUnit> implements SysUserUnitService {

  private static final String NON_EXIST_MESSAGE = "用户组织关系表[%s]不存在";

  @Override
  public SysUserUnit selectExtensionById(Serializable id) {
    SysUserUnit entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysUserUnit> searchExtensionPageList(SysUserUnitQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysUserUnit(SysUserUnit entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysUserUnit(Serializable id, SysUserUnit entity) {
    SysUserUnit existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void cascadeUpdate(List<SysUserUnit> existEntityList, List<SysUserUnit> updateEntityList) {
    Map<String, SysUserUnit> existEntityMap = existEntityList.stream().collect(Collectors.toMap(sysUserUnit -> String.format("%d,%d", sysUserUnit.getUserId(), sysUserUnit.getUnitId()), Function.identity()));
    Map<String, SysUserUnit> updateEntityMap = updateEntityList.stream().collect(Collectors.toMap(sysUserUnit -> String.format("%d,%d", sysUserUnit.getUserId(), sysUserUnit.getUnitId()), Function.identity()));

    List<SysUserUnit> addList = Lists.newArrayList();
    List<SysUserUnit> removeList = Lists.newArrayList();

    Iterator<SysUserUnit> existEntityIter = existEntityList.iterator();
    Iterator<SysUserUnit> updateEntityIter = updateEntityList.iterator();

    // 根据待更新的实体列表，找出需要新增的实体
    // 由于是userId、unitId为联合主键，对于UserUnit来说不需要更新，只有添加和删除操作
    while(updateEntityIter.hasNext()) {
      SysUserUnit updateEntity = updateEntityIter.next();
      String updateEntityKey = String.format("%d,%d", updateEntity.getUserId(), updateEntity.getUnitId());
      if (!existEntityMap.containsKey(updateEntityKey)) {
        addList.add(updateEntity);
      }
    }

    // 根据已经存在的实体，找出需要删除的实体
    while (existEntityIter.hasNext()) {
      SysUserUnit existEntity = existEntityIter.next();
      String existEntityKey = String.format("%d,%d", existEntity.getUserId(), existEntity.getUnitId());
      if (!updateEntityMap.containsKey(existEntityKey)) {
        removeList.add(existEntity);
      }
    }

    for (SysUserUnit removeUserUnit : removeList) {
      Map<String, Object> removeConditionMap = new HashMap<>();
      removeConditionMap.put("user_id", removeUserUnit.getUserId());
      removeConditionMap.put("unit_id", removeUserUnit.getUnitId());
      this.removeByMap(removeConditionMap);
    }
    this.saveBatch(addList);
  }

  @Override
  public void changeSysUserUnitStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysUserUnit::getStatus, status)
        .set(SysUserUnit::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysUserUnit::getUpdateDate, LocalDateTime.now())
        .eq(SysUserUnit::getId, id).update();

    if (!result) {
      throw BusinessException
          .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

  @Override
  public boolean saveOrUpdateBatch(List<SysUserUnitSaveCommand> commands) {
    Set<Serializable> userIds = new HashSet<>();
    Set<Serializable> unitIds = new HashSet<>();
    for (SysUserUnitSaveCommand sysUserSaveCommand : commands) {
      userIds.add(sysUserSaveCommand.getUserId());
      unitIds.add(sysUserSaveCommand.getUnitId());
    }
    //存在不同用户，即部门修改用户关系
    if (userIds.size() > 1) {
      this.lambdaUpdate().in(SysUserUnit::getUnitId, unitIds).remove();
      return this.saveBatch(SysUserUnitSaveCommandAssembler.INSTANCE.parseList(commands));
    } else {
      //不存在不同用户，即用户修改部门关系
      this.lambdaUpdate().in(SysUserUnit::getUserId, userIds).remove();
      return this.saveBatch(SysUserUnitSaveCommandAssembler.INSTANCE.parseList(commands));
    }
  }
}