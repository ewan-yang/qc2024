package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.PlanQueryCommand;
import com.tecpie.platform.report.entity.PlanStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 停电计划统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface PlanStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<PlanStatistics>
     */
    List<PlanStatistics> searchExtensionPageList(@Param("condition") PlanQueryCommand command);


}