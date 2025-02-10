package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysRoleQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.mapper.SysRoleMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysRoleService extends IBaseService<SysRoleMapper, SysRole> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysRole
   */
  SysRole selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysRole>
   */
  List<SysRole> searchExtensionPageList(SysRoleQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysRole(SysRole entity);

  /**
   * 更新
   * 更新角色时，对于角色与用户、角色与权限的关联关系如果有去除的，采用的是物理删除
   *
   * @param id
   * @param entity
   */
  void updateSysRole(Serializable id, SysRole entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysRoleStatus(Serializable id, Integer status);

  /**
   * 根据用户id集合查询相应的角色集合
   *
   * @param userIdList 用户标识列表
   * @return {@link List}<{@link SysRole}>
   */
  List<SysRole> listByUserIdList(List<Serializable> userIdList);

}