package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.ReleaseDurationQueryCommand;
import com.tecpie.platform.report.entity.ReleaseDurationStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 下达时长统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface ReleaseDurationStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<ReleaseDurationStatistics>
     */
    List<ReleaseDurationStatistics> searchExtensionPageList(@Param("condition") ReleaseDurationQueryCommand command);


}