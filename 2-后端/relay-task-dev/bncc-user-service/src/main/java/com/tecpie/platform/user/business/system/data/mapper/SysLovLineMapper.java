package com.tecpie.platform.user.business.system.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.data.api.command.query.SysLovLineQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * LOV明细行 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Repository
public interface SysLovLineMapper extends BaseMapper<SysLovLine> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysLovLine
   */
  SysLovLine selectExtensionById(@Param("id") Serializable id);

  /**
   * 根据 lovId 查询 Lov 明细数据
   * @param lovId
   * @return
   */
  List<SysLovLine> selectByLovId(@Param("lovId") Serializable lovId);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysLovLine>
   */
  List<SysLovLine> searchExtensionPageList(@Param("condition") SysLovLineQueryCommand command);

}