package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.CreateDurationQueryCommand;
import com.tecpie.platform.report.entity.CreateDurationStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 创建时长统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface CreateDurationStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<CreateDurationStatistics>
     */
    List<CreateDurationStatistics> searchExtensionPageList(@Param("condition") CreateDurationQueryCommand command);

}