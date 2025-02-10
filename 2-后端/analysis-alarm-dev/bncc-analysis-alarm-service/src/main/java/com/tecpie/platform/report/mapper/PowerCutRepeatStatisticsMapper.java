package com.tecpie.platform.report.mapper;

import com.tecpie.platform.report.api.command.query.PowerCutRepeatQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutRepeatStatisticsResource;
import com.tecpie.platform.report.entity.PowerCutRepeatDetail;
import com.tecpie.platform.report.entity.PowerCutRepeatStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 重复停电数据统计 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Repository
public interface PowerCutRepeatStatisticsMapper {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<PowerCutRepeatStatistics>
     */
    List<PowerCutRepeatStatistics> searchExtensionPageList(@Param("condition") PowerCutRepeatQueryCommand command);


    /**
     * 按台区获取详细停电信息
     *
     * @param command 命令
     * @return {@link List}<{@link PowerCutRepeatStatisticsResource}>
     */
    List<PowerCutRepeatDetail> getDetailByRegion(@Param("condition") PowerCutRepeatQueryCommand command);
}