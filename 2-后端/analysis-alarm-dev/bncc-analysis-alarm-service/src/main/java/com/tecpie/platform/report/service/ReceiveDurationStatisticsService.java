package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.ReceiveDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReceiveDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReceiveDurationTotalResource;
import com.tecpie.platform.report.entity.ReceiveDurationStatistics;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 接收时长数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface ReceiveDurationStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<ReceiveDurationStatistics>
     */
    List<ReceiveDurationStatistics> searchExtensionPageList(ReceiveDurationPageQueryCommand command);

    /**
     * 接收时长统计信息
     *
     * @param command 统计查询参数
     * @return ReceiveDurationTotalResource
     */
    ReceiveDurationTotalResource getReceiveDurationTotal(ReceiveDurationQueryCommand command);

    /**
     * 统计分析查询-接受时长统计-接受时长列表
     *
     * @param command  命令
     * @param response 响应
     */
    void receiveDurationStatisticsExport(ReceiveDurationPageQueryCommand command, HttpServletResponse response);

    void setMinFeedTime(List<ReceiveDurationStatistics> receiveDurationList);

    /**
     * 接收时长统计信息导出
     * @param regionTimeMap
     * @param userTypeTimeMap
     * @param response
     */
    void downLoad(Map<String, Map<String, Integer>> regionTimeMap, Map<String, Map<String, Integer>> userTypeTimeMap, HttpServletResponse response);
}