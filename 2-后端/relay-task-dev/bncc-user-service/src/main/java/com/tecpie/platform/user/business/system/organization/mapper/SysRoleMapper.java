package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysRoleQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysRole
   */
  SysRole selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysRole>
   */
  List<SysRole> searchExtensionPageList(@Param("condition") SysRoleQueryCommand command);

  /**
   * 根据用户id集合查询相应的角色集合
   *
   * @param userIdList 用户标识列表
   * @return {@link List}<{@link SysRole}>
   */
  List<SysRole> listByUserIdList(@Param("userIdList") List<Serializable> userIdList);

}