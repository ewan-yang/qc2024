package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysRolePermissionQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色与系统权限关联表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysRolePermission
   */
  SysRolePermission selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysRolePermission>
   */
  List<SysRolePermission> searchExtensionPageList(@Param("condition") SysRolePermissionQueryCommand command);

}