package com.tecpie.platform.user.business.system.function.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.function.api.command.query.SysOperationQueryCommand;
import com.tecpie.platform.user.business.system.function.entity.SysOperation;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 操作信息表 数据映射接口
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Repository
public interface SysOperationMapper extends BaseMapper<SysOperation> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysOperation
   */
  SysOperation selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysOperation>
   */
  List<SysOperation> searchExtensionPageList(@Param("condition") SysOperationQueryCommand command);

}