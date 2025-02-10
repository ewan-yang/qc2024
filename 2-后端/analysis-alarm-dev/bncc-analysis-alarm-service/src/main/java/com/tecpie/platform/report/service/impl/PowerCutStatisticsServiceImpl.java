package com.tecpie.platform.report.service.impl;

import com.tecpie.platform.common.feign.lov.SysLovFeignClient;
import com.tecpie.platform.common.util.AnalysisAlarmUtil;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.PowerCutPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutStatisticsResource;
import com.tecpie.platform.report.api.resource.PowerCutTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PowerCutStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PowerCutStatistics;
import com.tecpie.platform.report.mapper.PowerCutStatisticsMapper;
import com.tecpie.platform.report.service.PowerCutStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

/**
 * 停电数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class PowerCutStatisticsServiceImpl implements PowerCutStatisticsService {

    private PowerCutStatisticsMapper mapper;
    private SysLovFeignClient sysLovFeignClient;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setPowerCutStatisticsServiceImpl(PowerCutStatisticsMapper mapper, SysLovFeignClient sysLovFeignClient,
                                                 ExcelTransfer excelTransfer) {
        this.mapper = mapper;
        this.sysLovFeignClient = sysLovFeignClient;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<PowerCutStatistics> searchExtensionPageList(PowerCutQueryCommand command) {
        command.initDateParam();
        return mapper.searchExtensionPageList(command);
    }

    @Override
    public PowerCutTotalResource getPowerCutTotal(PowerCutQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<PowerCutStatistics> powerCutList = mapper.searchExtensionPageList(command);
        // 去年同期  确认时间减1年
        command.setStartDateBegin(command.getStartDateBegin().plusYears(-1));
        command.setStartDateEnd(command.getStartDateEnd().plusYears(-1));
        List<PowerCutStatistics> preYearDataList = mapper.searchExtensionPageList(command);
        // 获取上月数据，用于计算同比
        command.setStartDateBegin(command.getStartDateBegin().plusYears(1).minusMonths(1).withDayOfMonth(1));
        command.setStartDateEnd(command.getStartDateEnd().plusYears(1).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
        List<PowerCutStatistics> lastMonthDataList = mapper.searchExtensionPageList(command);
        // 用户类型字典Map
        Map<String, String> taskReasonMap = AnalysisAlarmUtil.getLovLileCodeMap(sysLovFeignClient.searchListByCode("task_reason").getData());
        // 统计信息对象
        PowerCutTotalResource totalResource = new PowerCutTotalResource();
        totalResource.initData(
                PowerCutStatisticsResourceAssembler.INSTANCE.parseList(powerCutList),
                PowerCutStatisticsResourceAssembler.INSTANCE.parseList(preYearDataList),
                PowerCutStatisticsResourceAssembler.INSTANCE.parseList(lastMonthDataList),
                taskReasonMap);
        return totalResource;
    }

    @Override
    public void powerCutStatisticsExport(PowerCutPageQueryCommand command, HttpServletResponse response) {
        List<PowerCutStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<PowerCutStatisticsResource> list = PowerCutStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "停电统计列表", "sheet1", PowerCutStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}