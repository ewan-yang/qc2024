package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.ReceiveDurationQueryCommand;
import com.tecpie.platform.report.entity.ReceiveDurationStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 接收时长统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface ReceiveDurationStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<ReceiveDurationStatistics>
     */
    List<ReceiveDurationStatistics> searchExtensionPageList(@Param("condition") ReceiveDurationQueryCommand command);


}