package com.tecpie.platform.report.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.PowerCutRepeatPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutRepeatQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutRepeatStatisticsResource;
import com.tecpie.platform.report.api.resource.PowerCutRepeatTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PowerCutRepeatStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PowerCutRepeatDetail;
import com.tecpie.platform.report.entity.PowerCutRepeatStatistics;
import com.tecpie.platform.report.mapper.PowerCutRepeatStatisticsMapper;
import com.tecpie.platform.report.service.PowerCutRepeatStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * 重复停电数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class PowerCutRepeatStatisticsServiceImpl implements PowerCutRepeatStatisticsService {

    private PowerCutRepeatStatisticsMapper mapper;
    private ExcelTransfer<PowerCutRepeatStatisticsResource> excelTransfer;

    @Autowired
    public void setPowerCutRepeatStatisticsServiceImpl(PowerCutRepeatStatisticsMapper mapper, ExcelTransfer<PowerCutRepeatStatisticsResource> excelTransfer) {
        this.mapper = mapper;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<PowerCutRepeatStatistics> searchExtensionPageList(PowerCutRepeatQueryCommand command) {
        command.initDateParam();
        return mapper.searchExtensionPageList(command);
    }

    @Override
    public PowerCutRepeatTotalResource getPowerCutRepeatTotal(PowerCutRepeatQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<PowerCutRepeatStatistics> powerCutRepeatList = mapper.searchExtensionPageList(command);
        // 去年同期  停电时间减1年
        command.setStartDateBegin(command.getStartDateBegin().plusYears(-1));
        command.setStartDateEnd(LocalDate.of(command.getStartDateBegin().getYear(), 12, 31));
        List<PowerCutRepeatStatistics> preYearDataList = mapper.searchExtensionPageList(command);
        // 统计信息对象
        PowerCutRepeatTotalResource totalResource = new PowerCutRepeatTotalResource();
        totalResource.initData(PowerCutRepeatStatisticsResourceAssembler.INSTANCE.parseList(powerCutRepeatList), PowerCutRepeatStatisticsResourceAssembler.INSTANCE.parseList(preYearDataList));
        return totalResource;
    }

    @Override
    public void powerCutRepeatStatisticsExport(PowerCutRepeatPageQueryCommand command, HttpServletResponse response) {
        List<PowerCutRepeatStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<PowerCutRepeatStatisticsResource> list = PowerCutRepeatStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response,list,"重复停电统计列表","sheet1",PowerCutRepeatStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw BusinessException.build("导出异常");
        }
    }

    @Override
    public List<PowerCutRepeatDetail> getDetailByRegion(PowerCutRepeatQueryCommand command) {
        return mapper.getDetailByRegion(command);
    }


}