package com.tecpie.platform.user.business.system.sys_operation_log.mapper;

import com.tecpie.platform.user.business.system.sys_operation_log.entity.OperationLog;
import com.tecpie.platform.user.business.system.sys_operation_log.api.command.query.OperationLogQueryCommand;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志表 数据映射接口
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return OperationLog
   */
  OperationLog selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<OperationLog>
   */
  List<OperationLog> searchExtensionPageList(@Param("condition") OperationLogQueryCommand command);

}