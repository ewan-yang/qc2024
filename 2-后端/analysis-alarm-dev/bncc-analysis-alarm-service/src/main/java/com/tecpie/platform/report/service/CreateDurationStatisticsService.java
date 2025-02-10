package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.CreateDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.CreateDurationQueryCommand;
import com.tecpie.platform.report.api.resource.CreateDurationTotalResource;
import com.tecpie.platform.report.entity.CreateDurationStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 创建时长数据统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
public interface CreateDurationStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<CreateDurationStatistics>
     */
    List<CreateDurationStatistics> searchExtensionPageList(CreateDurationQueryCommand command);

    /**
     * 创建时长统计信息
     *
     * @param command 统计查询参数
     * @return CreateDurationTotalResource
     */
    CreateDurationTotalResource getCreateDurationTotal(CreateDurationQueryCommand command);


    /**
     * 统计分析查询-创建时长统计-创建时长列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void createDurationStatisticsExport(CreateDurationPageQueryCommand command, HttpServletResponse response);
}