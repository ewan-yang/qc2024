package com.tecpie.platform.report.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.platform.common.feign.lov.SysLovFeignClient;
import com.tecpie.platform.common.feign.task_user.TaskUserFeedbackLogFeignClient;
import com.tecpie.platform.common.util.AnalysisAlarmUtil;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.ReceiveDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReceiveDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReceiveDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.ReceiveDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.ReceiveDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.ReceiveDurationStatistics;
import com.tecpie.platform.report.mapper.ReceiveDurationStatisticsMapper;
import com.tecpie.platform.report.service.ReceiveDurationStatisticsService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserFeedbackLogResource;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 接收时长数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class ReceiveDurationStatisticsServiceImpl implements ReceiveDurationStatisticsService {

    private ReceiveDurationStatisticsMapper mapper;
    private SysLovFeignClient sysLovFeignClient;
    private ExcelTransfer excelTransfer;
    private TaskUserFeedbackLogFeignClient taskUserFeedbackLogFeignClient;

    @Autowired
    public void setReceiveDurationStatisticsServiceImpl(ReceiveDurationStatisticsMapper mapper,
                                                        SysLovFeignClient sysLovFeignClient,
                                                        ExcelTransfer excelTransfer,
                                                        TaskUserFeedbackLogFeignClient taskUserFeedbackLogFeignClient) {
        this.mapper = mapper;
        this.sysLovFeignClient = sysLovFeignClient;
        this.excelTransfer = excelTransfer;
        this.taskUserFeedbackLogFeignClient = taskUserFeedbackLogFeignClient;
    }

    @Override
    public List<ReceiveDurationStatistics> searchExtensionPageList(ReceiveDurationPageQueryCommand command) {
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        ReceiveDurationQueryCommand condition = command.getCondition();
        condition.initDateParam();
        return this.mapper.searchExtensionPageList(condition);
    }

    @Override
    public ReceiveDurationTotalResource getReceiveDurationTotal(ReceiveDurationQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<ReceiveDurationStatistics> receiveDurationList = this.mapper.searchExtensionPageList(command);
        if (CollectionUtils.isNotEmpty(receiveDurationList)) {
            this.setMinFeedTime(receiveDurationList);
        }
        // 用户类型字典Map
        Map<String, String> userTypeMap = AnalysisAlarmUtil.getLovLileCodeMap(sysLovFeignClient.searchListByCode("task_user_type").getData());
        // 统计信息对象
        ReceiveDurationTotalResource totalResource = new ReceiveDurationTotalResource();
        totalResource.initData(ReceiveDurationStatisticsResourceAssembler.INSTANCE.parseList(receiveDurationList), userTypeMap);
        return totalResource;
    }

    @Override
    public void receiveDurationStatisticsExport(ReceiveDurationPageQueryCommand command, HttpServletResponse response) {
        List<ReceiveDurationStatistics> entityList = this.searchExtensionPageList(command);
        List<ReceiveDurationStatisticsResource> list = ReceiveDurationStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "接受时长统计列表", "sheet1", ReceiveDurationStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setMinFeedTime(List<ReceiveDurationStatistics> receiveDurationList) {
        List<Serializable> taskUserIdList = receiveDurationList.stream().map(ReceiveDurationStatistics::getId).distinct().collect(Collectors.toList());
        // 查询反馈日志表
        TaskUserFeedbackLogQueryCommand taskUserFeedbackLogQueryCommand = new TaskUserFeedbackLogQueryCommand();
        taskUserFeedbackLogQueryCommand.setTaskUserIdList(taskUserIdList);
        List<TaskUserFeedbackLogResource> feedbackLogResourceList = taskUserFeedbackLogFeignClient.searchTaskUserFeedbackLogList(taskUserFeedbackLogQueryCommand).getData();
        feedbackLogResourceList = feedbackLogResourceList.stream().collect(Collectors.groupingBy(TaskUserFeedbackLogResource::getTaskUserId))
                .values().stream().map(taskUserFeedbackLogResources -> taskUserFeedbackLogResources.stream().min(Comparator.comparing(TaskUserFeedbackLogResource::getCreateDate))
                        .orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        for (ReceiveDurationStatistics receiveDurationStatistics : receiveDurationList) {
            Serializable id = receiveDurationStatistics.getId();
            feedbackLogResourceList.stream().filter(t -> t.getTaskUserId().equals(id.toString())).findFirst().ifPresent(t -> {
                receiveDurationStatistics.setFeedbackTime(t.getCreateDate());
            });
        }
    }

    @Override
    public void downLoad(Map<String, Map<String, Integer>> regionTimeMap, Map<String, Map<String, Integer>> userTypeTimeMap, HttpServletResponse response) {

        String dayTypeOne = "时长小于4天";
        String dayTypeTwo = "时长4至7天";
        String dayTypeThree = "时长大于7天";
        String countNum = "回执个数";

        try {
            Workbook workbook = new XSSFWorkbook();

            // 写入各区域平均接收时长统计分析数据
            Sheet sheet1 = workbook.createSheet("区域分析");

            sheet1.createRow(0).createCell(0).setCellValue("所属台区");
            sheet1.getRow(0).createCell(1).setCellValue(dayTypeOne);
            sheet1.getRow(0).createCell(2).setCellValue(dayTypeTwo);
            sheet1.getRow(0).createCell(3).setCellValue(dayTypeThree);

            int rowNum1 = 1;
            for (String key : regionTimeMap.keySet()) {
                Map<String, Integer> countMap1 = regionTimeMap.get(key);
                sheet1.createRow(rowNum1).createCell(0).setCellValue(key);
                sheet1.getRow(rowNum1).createCell(1).setCellValue(countMap1.getOrDefault(dayTypeOne, 0));
                sheet1.getRow(rowNum1).createCell(2).setCellValue(countMap1.getOrDefault(dayTypeTwo, 0));
                sheet1.getRow(rowNum1).createCell(3).setCellValue(countMap1.getOrDefault(dayTypeThree, 0));
                rowNum1++;
            }

            // 写入各用户类型平均接收时长统计分析数据
            Sheet sheet2 = workbook.createSheet("用户类型分析");
            sheet2.createRow(0).createCell(0).setCellValue("用户类型");
            sheet2.getRow(0).createCell(1).setCellValue(dayTypeOne);
            sheet2.getRow(0).createCell(2).setCellValue(dayTypeTwo);
            sheet2.getRow(0).createCell(3).setCellValue(dayTypeThree);
            sheet2.getRow(0).createCell(4).setCellValue(countNum);

            int rowNum2 = 1;
            for (String key : userTypeTimeMap.keySet()) {
                Map<String, Integer> countMap2 = userTypeTimeMap.get(key);

                sheet2.createRow(rowNum2).createCell(0).setCellValue(key);
                sheet2.getRow(rowNum2).createCell(1).setCellValue(countMap2.getOrDefault(dayTypeOne, 0));
                sheet2.getRow(rowNum2).createCell(2).setCellValue(countMap2.getOrDefault(dayTypeTwo, 0));
                sheet2.getRow(rowNum2).createCell(3).setCellValue(countMap2.getOrDefault(dayTypeThree, 0));
                sheet2.getRow(rowNum2).createCell(4).setCellValue(countMap2.getOrDefault(countNum, 0));
                rowNum2++;
            }

            // 导出Excel
            String filename = URLEncoder.encode( "接收时长统计图表", StandardCharsets.UTF_8);
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