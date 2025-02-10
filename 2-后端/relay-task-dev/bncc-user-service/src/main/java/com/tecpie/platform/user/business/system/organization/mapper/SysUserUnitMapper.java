package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserUnitQueryCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户组织关系表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysUserUnitMapper extends BaseMapper<SysUserUnit> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserUnit
   */
  SysUserUnit selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserUnit>
   */
  List<SysUserUnit> searchExtensionPageList(@Param("condition") SysUserUnitQueryCommand command);

}