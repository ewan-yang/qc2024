package com.tecpie.platform.user.business.system.function.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.function.api.command.query.SysPermissionQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统权限表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysPermission
   */
  SysPermission selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysPermission>
   */
  List<SysPermission> searchExtensionPageList(@Param("condition") SysPermissionQueryCommand command);

  /**
   * 根据角色id列表查询权限列表 TODO
   * @param roleIdList
   * @return List<SysPermission>
   */
  List<SysPermission> selectListByRoleList(@Param("roleIdList") List<Serializable> roleIdList);

}