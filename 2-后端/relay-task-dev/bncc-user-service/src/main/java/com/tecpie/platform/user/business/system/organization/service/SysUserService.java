package com.tecpie.platform.user.business.system.organization.service;

import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.update.PasswordUpdateCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserMapper;
import com.tecpie.starter.feign.entity.SecurityUser;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import java.io.Serializable;
import java.util.List;

/**
 * 用户信息表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysUserService extends IBaseService<SysUserMapper, SysUser> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUser
   */
  SysUser selectExtensionById(Serializable id);

  SecurityUser selectSecurityUser(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUser>
   */
  List<SysUser> searchExtensionPageList(SysUserQueryCommand command);

  /**
   * 根据登录名查询用户及角色权限
   *
   * @param code 登录名
   * @return
   */
  SysUser selectAuthByCode(String code);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysUser(SysUser entity);

  /**
   * 更新
   * 更新用户时，对于用户与角色、用户与组织机构的关联关系如果有去除的，采用的是物理删除
   *
   * @param id
   * @param entity
   */
  void updateSysUser(Serializable id, SysUser entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysUserStatus(Serializable id, Integer status);

  List<SysUser> searchListByUnit(SysUserQueryCommand condition);

  List<SysUser> getUserListByIds(List<Long> ids);

  /**
   * 修改密码，需要账号，原密码和新密码
   *
   * @param command 命令
   */
  void updatePassword(PasswordUpdateCommand command);
}