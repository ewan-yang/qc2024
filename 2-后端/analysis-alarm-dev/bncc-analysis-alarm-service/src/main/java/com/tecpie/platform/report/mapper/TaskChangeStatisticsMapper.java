package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.TaskChangeQueryCommand;
import com.tecpie.platform.report.entity.TaskChangeStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知变更统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface TaskChangeStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskChangeStatistics>
     */
    List<TaskChangeStatistics> searchExtensionPageList(@Param("condition") TaskChangeQueryCommand command);


}