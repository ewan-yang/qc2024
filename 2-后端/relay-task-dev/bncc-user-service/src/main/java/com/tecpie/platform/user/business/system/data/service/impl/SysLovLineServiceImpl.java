package com.tecpie.platform.user.business.system.data.service.impl;

import com.google.common.collect.Lists;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.data.api.command.query.SysLovLineQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.mapper.SysLovLineMapper;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LOV明细行 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLovLineServiceImpl extends BaseServiceImpl<SysLovLineMapper, SysLovLine> implements SysLovLineService {

  private static final String NON_EXIST_MESSAGE = "LOV明细行[%s]不存在";

  @Override
  public SysLovLine selectExtensionById(Serializable id) {
    SysLovLine entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysLovLine> searchExtensionPageList(SysLovLineQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysLovLine(SysLovLine entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysLovLine(Serializable id, SysLovLine entity) {
    SysLovLine existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  /**
   * 重载了父类的方法，在删除时使用了逻辑删除
   * @param existEntityList
   * @param updateEntityList
   */
  @Override
  public void cascadeUpdate(List<SysLovLine> existEntityList, List<SysLovLine> updateEntityList) {
      Map<Serializable, SysLovLine> existEntityMap = existEntityList.stream().collect(Collectors.toMap((entityx) -> entityx.getId().toString(), Function.identity()));
    List<SysLovLine> addList = Lists.newArrayList();
    List<SysLovLine> updateList = Lists.newArrayList();
    Iterator<SysLovLine> updateEntityIter = updateEntityList.iterator();

    while(updateEntityIter.hasNext()) {
      SysLovLine entity = updateEntityIter.next();
      if (entity.getId() == null) {
        addList.add(entity);
      } else if (existEntityMap.containsKey(entity.getId().toString())) {
        updateList.add(entity);
        existEntityMap.remove(entity.getId());
      }
    }

    this.logicRemoveByIds(existEntityMap.keySet());
    this.saveBatch(addList);
    this.updateBatchById(updateList);
  }

  @Override
  public void changeSysLovLineStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysLovLine::getStatus, status)
        .set(SysLovLine::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysLovLine::getUpdateDate, LocalDateTime.now())
        .eq(SysLovLine::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

}