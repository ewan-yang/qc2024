package com.tecpie.platform.user.business.system.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.data.api.command.query.SysLovQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLov;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * LOV定义表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysLovMapper extends BaseMapper<SysLov> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysLov
   */
  SysLov selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysLov>
   */
  List<SysLov> searchExtensionPageList(@Param("condition") SysLovQueryCommand command);

}