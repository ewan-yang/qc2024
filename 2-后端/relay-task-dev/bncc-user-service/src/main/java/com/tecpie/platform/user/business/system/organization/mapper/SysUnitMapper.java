package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.organization.api.command.query.SysUnitQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUnitResource;
import com.tecpie.platform.user.business.system.organization.entity.SysUnit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 组织结构表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysUnitMapper extends BaseMapper<SysUnit> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUnit
   */
  SysUnit selectExtensionById(@Param("id") Serializable id);

  /**
   * 获取 parentId 所属的子节点
   * @param parentId
   * @return
   */
  List<SysUnit> selectExtensionByParentId(@Param("parentId") Serializable parentId);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUnit>
   */
  List<SysUnitResource> searchExtensionPageList(@Param("condition") SysUnitQueryCommand command);

  /**
   * 根据用户id获取用户单位信息集合
   *
   * @param userIdList 用户标识列表
   * @return {@link List}<{@link SysUnit}>
   */
  List<SysUnit> listByUserIdList(@Param("userIdList") List<Serializable> userIdList);
}