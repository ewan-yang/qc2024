package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.PowerCutRepeatPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutRepeatQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutRepeatStatisticsResource;
import com.tecpie.platform.report.api.resource.PowerCutRepeatTotalResource;
import com.tecpie.platform.report.entity.PowerCutRepeatDetail;
import com.tecpie.platform.report.entity.PowerCutRepeatStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 重复停电数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface PowerCutRepeatStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<PowerCutRepeatStatistics>
     */
    List<PowerCutRepeatStatistics> searchExtensionPageList(PowerCutRepeatQueryCommand command);

    /**
     * 重复停电数据统计信息
     *
     * @param command 统计查询参数
     * @return PowerCutRepeatTotalResource
     */
    PowerCutRepeatTotalResource getPowerCutRepeatTotal(PowerCutRepeatQueryCommand command);

    /**
     * 统计分析查询-重复停电统计-停电列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void powerCutRepeatStatisticsExport(PowerCutRepeatPageQueryCommand command, HttpServletResponse response);

    /**
     * 按台区获取详细停电信息
     *
     * @param command 命令
     * @return {@link PowerCutRepeatStatisticsResource}
     */
    List<PowerCutRepeatDetail> getDetailByRegion(PowerCutRepeatQueryCommand command);
}