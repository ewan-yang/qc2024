package com.tecpie.platform.plan_item.mapper;

import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.api.command.query.PlanItemQueryCommand;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.List;

/**
 * 停电计划项表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Repository
public interface PlanItemMapper extends BaseMapper<PlanItem> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return PlanItem
   */
  PlanItem selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<PlanItem>
   */
  List<PlanItem> searchExtensionPageList(@Param("condition") PlanItemQueryCommand command);

}