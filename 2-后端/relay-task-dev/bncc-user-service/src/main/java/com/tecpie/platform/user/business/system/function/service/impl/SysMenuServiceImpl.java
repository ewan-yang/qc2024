package com.tecpie.platform.user.business.system.function.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.function.api.command.query.SysMenuQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysMenu;
import com.tecpie.platform.user.business.system.function.mapper.SysMenuMapper;
import com.tecpie.platform.user.business.system.function.service.SysMenuService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务类实现
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

  private static final String NON_EXIST_MESSAGE = "菜单定义[%s]不存在";

  @Override
  public SysMenu selectExtensionById(Serializable id) {
    SysMenu entity = this.baseMapper.selectExtensionById(id);
    if (entity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
    return entity;
  }

  @Override
  public List<SysMenu> searchExtensionPageList(SysMenuQueryCommand command) {
    return this.baseMapper.searchExtensionPageList(command);
  }

  @Override
  public Serializable saveSysMenu(SysMenu entity) {
    this.save(entity);
    return entity.getId();
  }

  @Override
  public void updateSysMenu(Serializable id, SysMenu entity) {
    SysMenu existEntity = this.baseMapper.selectById(id);
    if (existEntity == null) {
      throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }

    entity.setId(id);
    this.updateById(entity);
  }

  @Override
  public void changeSysMenuStatus(Serializable id, Integer status) {
    boolean result = this.lambdaUpdate()
        .set(SysMenu::getStatus, status)
        .set(SysMenu::getUpdateBy, TecpieSecurityUtil.getUser().getId())
        .set(SysMenu::getUpdateDate, LocalDateTime.now())
        .eq(SysMenu::getId, id).update();

    if (!result) {
        throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
    }
  }

}