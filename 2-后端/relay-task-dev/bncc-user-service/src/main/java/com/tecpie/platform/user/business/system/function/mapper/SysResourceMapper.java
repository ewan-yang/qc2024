package com.tecpie.platform.user.business.system.function.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.function.api.command.query.SysResourceQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysResource;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统资源表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Repository
public interface SysResourceMapper extends BaseMapper<SysResource> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysResource
   */
  SysResource selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysResource>
   */
  List<SysResource> searchExtensionPageList(@Param("condition") SysResourceQueryCommand command);

}