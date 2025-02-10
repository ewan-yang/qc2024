package com.tecpie.platform.report.service;

import com.tecpie.platform.report.api.command.query.PowerCutPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutTotalResource;
import com.tecpie.platform.report.entity.PowerCutStatistics;
import com.tecpie.platform.report.mapper.PowerCutStatisticsMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 停电统计 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
public interface PowerCutStatisticsService {

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<PowerCutStatistics>
     */
    List<PowerCutStatistics> searchExtensionPageList(PowerCutQueryCommand command);

    /**
     * 接收时长统计信息
     *
     * @param command 统计查询参数
     * @return PowerCutTotalResource
     */
    PowerCutTotalResource getPowerCutTotal(PowerCutQueryCommand command);

    /**
     * 统计分析查询-停电统计-停电列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void powerCutStatisticsExport(PowerCutPageQueryCommand command, HttpServletResponse response);
}