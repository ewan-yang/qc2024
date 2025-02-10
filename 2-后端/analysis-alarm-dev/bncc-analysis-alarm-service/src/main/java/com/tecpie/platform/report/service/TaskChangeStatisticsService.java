package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.TaskChangePageQueryCommand;
import com.tecpie.platform.report.api.command.query.TaskChangeQueryCommand;
import com.tecpie.platform.report.api.resource.TaskChangeTotalResource;
import com.tecpie.platform.report.entity.TaskChangeStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通知变更数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface TaskChangeStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskChangeStatistics>
     */
    List<TaskChangeStatistics> searchExtensionPageList(TaskChangeQueryCommand command);

    /**
     * 通知变更统计信息
     *
     * @param command 统计查询参数
     * @return TaskChangeTotalResource
     */
    TaskChangeTotalResource getTaskChangeTotal(TaskChangeQueryCommand command);

    /**
     * 统计分析查询-通知变更统计-通知变更列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void taskChangeStatisticsExport(TaskChangePageQueryCommand command, HttpServletResponse response);
}