package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserRoleQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户角色关联表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserRole
   */
  SysUserRole selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserRole>
   */
  List<SysUserRole> searchExtensionPageList(@Param("condition") SysUserRoleQueryCommand command);

}