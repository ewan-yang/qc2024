package com.tecpie.platform.report.service.impl;

import com.tecpie.platform.common.feign.lov.SysLovFeignClient;
import com.tecpie.platform.common.util.AnalysisAlarmUtil;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.ReleaseDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReleaseDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReleaseDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.ReleaseDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.ReleaseDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.ReleaseDurationStatistics;
import com.tecpie.platform.report.mapper.ReleaseDurationStatisticsMapper;
import com.tecpie.platform.report.service.ReleaseDurationStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 下达时长数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class ReleaseDurationStatisticsServiceImpl implements ReleaseDurationStatisticsService {

    private ReleaseDurationStatisticsMapper mapper;
    private SysLovFeignClient sysLovFeignClient;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setReleaseDurationStatisticsServiceImpl(ReleaseDurationStatisticsMapper mapper,
                                                        SysLovFeignClient sysLovFeignClient,
                                                        ExcelTransfer excelTransfer) {
        this.mapper = mapper;
        this.sysLovFeignClient = sysLovFeignClient;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<ReleaseDurationStatistics> searchExtensionPageList(ReleaseDurationQueryCommand command) {
        command.initDateParam();
        return this.mapper.searchExtensionPageList(command);
    }

    @Override
    public ReleaseDurationTotalResource getReleaseDurationTotal(ReleaseDurationQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<ReleaseDurationStatistics> releaseDurationList = this.mapper.searchExtensionPageList(command);
        // 用户类型字典Map
        Map<String, String> userTypeMap = AnalysisAlarmUtil.getLovLileCodeMap(sysLovFeignClient.searchListByCode("task_user_type").getData());
        // 统计信息对象
        ReleaseDurationTotalResource totalResource = new ReleaseDurationTotalResource();
        totalResource.initData(ReleaseDurationStatisticsResourceAssembler.INSTANCE.parseList(releaseDurationList), userTypeMap);
        return totalResource;
    }

    @Override
    public void releaseDurationStatisticsExport(ReleaseDurationPageQueryCommand command, HttpServletResponse response) {
        List<ReleaseDurationStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<ReleaseDurationStatisticsResource> list = ReleaseDurationStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "下发时长统计列表", "sheet1", ReleaseDurationStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downLoad(Map<String, Map<String, Integer>> regionTimeMap, Map<String, Map<String, Integer>> userTypeTimeMap, HttpServletResponse response) {
        String dayTypeOne = "时长小于2天";
        String dayTypeTwo = "时长3至4天";
        String dayTypeThree = "时长5至6天";
        String dayTypeFour = "时长大于7天";

        try {
            Workbook workbook = new XSSFWorkbook();

            // 写入各区域平均接收时长统计分析数据
            Sheet sheet1 = workbook.createSheet("区域分析");

            sheet1.createRow(0).createCell(0).setCellValue("所属台区");
            sheet1.getRow(0).createCell(1).setCellValue(dayTypeOne);
            sheet1.getRow(0).createCell(2).setCellValue(dayTypeTwo);
            sheet1.getRow(0).createCell(3).setCellValue(dayTypeThree);
            sheet1.getRow(0).createCell(4).setCellValue(dayTypeFour);

            int rowNum1 = 1;
            for (String key : regionTimeMap.keySet()) {
                Map<String, Integer> countMap1 = regionTimeMap.get(key);
                sheet1.createRow(rowNum1).createCell(0).setCellValue(key);
                sheet1.getRow(rowNum1).createCell(1).setCellValue(countMap1.getOrDefault(dayTypeOne, 0));
                sheet1.getRow(rowNum1).createCell(2).setCellValue(countMap1.getOrDefault(dayTypeTwo, 0));
                sheet1.getRow(rowNum1).createCell(3).setCellValue(countMap1.getOrDefault(dayTypeThree, 0));
                sheet1.getRow(rowNum1).createCell(3).setCellValue(countMap1.getOrDefault(dayTypeFour, 0));
                rowNum1++;
            }

            // 写入各用户类型平均接收时长统计分析数据
            Sheet sheet2 = workbook.createSheet("用户类型分析");
            sheet2.createRow(0).createCell(0).setCellValue("用户类型");
            sheet2.getRow(0).createCell(1).setCellValue(dayTypeOne);
            sheet2.getRow(0).createCell(2).setCellValue(dayTypeTwo);
            sheet2.getRow(0).createCell(3).setCellValue(dayTypeThree);
            sheet2.getRow(0).createCell(4).setCellValue(dayTypeFour);

            int rowNum2 = 1;
            for (String key : userTypeTimeMap.keySet()) {
                Map<String, Integer> countMap2 = userTypeTimeMap.get(key);

                sheet2.createRow(rowNum2).createCell(0).setCellValue(key);
                sheet2.getRow(rowNum2).createCell(1).setCellValue(countMap2.getOrDefault(dayTypeOne, 0));
                sheet2.getRow(rowNum2).createCell(2).setCellValue(countMap2.getOrDefault(dayTypeTwo, 0));
                sheet2.getRow(rowNum2).createCell(3).setCellValue(countMap2.getOrDefault(dayTypeThree, 0));
                sheet2.getRow(rowNum2).createCell(4).setCellValue(countMap2.getOrDefault(dayTypeFour, 0));
                rowNum2++;
            }

            // 导出Excel
            String filename = URLEncoder.encode( "下发时长统计图表", StandardCharsets.UTF_8);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}