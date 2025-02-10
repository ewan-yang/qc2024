package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.PlanPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PlanQueryCommand;
import com.tecpie.platform.report.api.resource.PlanTotalResource;
import com.tecpie.platform.report.entity.PlanStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 停电计划数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface PlanStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<PlanStatistics>
     */
    List<PlanStatistics> searchExtensionPageList(PlanQueryCommand command);

    /**
     * 接收时长统计信息
     *
     * @param command 统计查询参数
     * @return PlanTotalResource
     */
    PlanTotalResource getPlanTotal(PlanQueryCommand command);

    /**
     * 统计分析查询-停电计划统计-导出
     *
     * @param command  检查条件
     * @param response 文件流响应
     */
    void planStatisticsExport(PlanPageQueryCommand command, HttpServletResponse response);
}