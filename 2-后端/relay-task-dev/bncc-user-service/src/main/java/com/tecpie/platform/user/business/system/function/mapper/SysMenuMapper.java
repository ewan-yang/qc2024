package com.tecpie.platform.user.business.system.function.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.function.api.command.query.SysMenuQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysMenu;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *  数据映射接口
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysMenu
   */
  SysMenu selectExtensionById(@Param("id") Serializable id);

  /**
   * 获取 parentId 所属的子节点
   * @param parentId
   * @return
   */
  List<SysMenu> selectExtensionByParentId(@Param("parentId") Serializable parentId);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysMenu>
   */
  List<SysMenu> searchExtensionPageList(@Param("condition") SysMenuQueryCommand command);

}