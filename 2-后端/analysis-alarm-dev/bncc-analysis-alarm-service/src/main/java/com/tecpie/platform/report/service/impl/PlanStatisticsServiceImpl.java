package com.tecpie.platform.report.service.impl;

import com.tecpie.platform.common.feign.lov.SysLovFeignClient;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.PlanPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PlanQueryCommand;
import com.tecpie.platform.report.api.resource.PlanStatisticsResource;
import com.tecpie.platform.report.api.resource.PlanTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PlanStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PlanStatistics;
import com.tecpie.platform.report.mapper.PlanStatisticsMapper;
import com.tecpie.platform.report.service.PlanStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class PlanStatisticsServiceImpl implements PlanStatisticsService {

    private PlanStatisticsMapper mapper;
    private SysLovFeignClient sysLovFeignClient;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setPlanStatisticsServiceImpl(PlanStatisticsMapper mapper, SysLovFeignClient sysLovFeignClient, ExcelTransfer excelTransfer) {
        this.mapper = mapper;
        this.sysLovFeignClient = sysLovFeignClient;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<PlanStatistics> searchExtensionPageList(PlanQueryCommand command) {
        command.initDateParam();
        return mapper.searchExtensionPageList(command);
    }

    @Override
    public PlanTotalResource getPlanTotal(PlanQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<PlanStatistics> planList = mapper.searchExtensionPageList(command);
        // 统计信息对象
        PlanTotalResource totalResource = new PlanTotalResource();
        totalResource.initData(PlanStatisticsResourceAssembler.INSTANCE.parseList(planList));
        return totalResource;
    }

    @Override
    public void planStatisticsExport(PlanPageQueryCommand command, HttpServletResponse response) {
        List<PlanStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<PlanStatisticsResource> list = PlanStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "停电计划统计列表", "sheet1", PlanStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}