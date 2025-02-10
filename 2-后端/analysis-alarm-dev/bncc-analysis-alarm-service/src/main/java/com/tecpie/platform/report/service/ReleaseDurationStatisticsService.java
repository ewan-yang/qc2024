package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.ReleaseDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReleaseDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReleaseDurationTotalResource;
import com.tecpie.platform.report.entity.ReleaseDurationStatistics;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 下达时长数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface ReleaseDurationStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<ReleaseDurationStatistics>
     */
    List<ReleaseDurationStatistics> searchExtensionPageList(ReleaseDurationQueryCommand command);

    /**
     * 下达时长统计信息
     *
     * @param command 统计查询参数
     * @return ReleaseDurationTotalResource
     */
    ReleaseDurationTotalResource getReleaseDurationTotal(ReleaseDurationQueryCommand command);


    /**
     * 统计分析查询-下发时长统计-下发时长列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void releaseDurationStatisticsExport(ReleaseDurationPageQueryCommand command, HttpServletResponse response);

    /**
     * 下发时长统计信息导出
     * @param regionTimeMap
     * @param userTypeTimeMap
     * @param response
     */
    void downLoad(Map<String, Map<String, Integer>> regionTimeMap, Map<String, Map<String, Integer>> userTypeTimeMap, HttpServletResponse response);

}