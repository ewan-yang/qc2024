package com.tecpie.platform.user.business.system.function.service;

import com.tecpie.platform.user.business.function.api.command.query.SysMenuQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysMenu;
import com.tecpie.platform.user.business.system.function.mapper.SysMenuMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 *  服务类接口
 *
 * @author tecpie
 * @since 2022-11-08
 */
public interface SysMenuService extends IBaseService<SysMenuMapper, SysMenu> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysMenu
   */
  SysMenu selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysMenu>
   */
  List<SysMenu> searchExtensionPageList(SysMenuQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysMenu(SysMenu entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysMenu(Serializable id, SysMenu entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysMenuStatus(Serializable id, Integer status);

}