package com.tecpie.platform.report.controller;

import cn.hutool.core.util.ObjectUtil;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.feign.task_user.TaskUserFeedbackLogFeignClient;
import com.tecpie.platform.report.api.command.query.ReceiveDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReceiveDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReceiveDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.ReceiveDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.ReceiveDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.ReceiveDurationStatistics;
import com.tecpie.platform.report.service.ReceiveDurationStatisticsService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.service.TaskAdvanceRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接收时长统计分析 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/receiveDurationStatistics")
@Tag(name = "接收时长统计分析接口定义")
public class ReceiveDurationStatisticsRestController {

    private ReceiveDurationStatisticsService receiveDurationStatisticsService;
    private TaskAdvanceRuleService taskAdvanceRuleService;

    @Autowired
    public void setReceiveDurationStatisticsRestController(ReceiveDurationStatisticsService receiveDurationStatisticsService,
                                                           TaskAdvanceRuleService taskAdvanceRuleService) {
        this.receiveDurationStatisticsService = receiveDurationStatisticsService;
        this.taskAdvanceRuleService = taskAdvanceRuleService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<ReceiveDurationStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody ReceiveDurationPageQueryCommand command) {
        // 查询分页数据
        List<ReceiveDurationStatistics> entityList = receiveDurationStatisticsService.searchExtensionPageList(command);
        receiveDurationStatisticsService.setMinFeedTime(entityList);
        // 查询下达风险预警值
        TaskAdvanceRule advanceRule = taskAdvanceRuleService.lambdaQuery().eq(TaskAdvanceRule::getRuleCode, "GZ0000002").one();
        if (ObjectUtil.isNotNull(advanceRule) && StringUtils.isNotBlank(advanceRule.getRuleCode())) {
            entityList.forEach(t -> t.setAlertDays(advanceRule.getDays()));
        }
        List<ReceiveDurationStatistics> list = entityList.stream()
                .sorted(Comparator.comparing(ReceiveDurationStatistics::getStartTime)).collect(Collectors.toList());
        // 构造分页结果
        return Result.success(entityList, ReceiveDurationStatisticsResourceAssembler.INSTANCE.parseList(list));
    }

    /**
     * 统计分析查询-接受时长统计-接受时长列表
     */
    @PostMapping("/export")
    @Operation(summary = "统计分析查询-接受时长统计-接受时长列表")
    public void receiveDurationStatisticsExport(@RequestBody ReceiveDurationPageQueryCommand command, HttpServletResponse response) {
        receiveDurationStatisticsService.receiveDurationStatisticsExport(command, response);
    }

    /**
     * 接收时长统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "接收时长统计信息")
    public Result<ReceiveDurationTotalResource> getCreateDurationTotal(@RequestBody ReceiveDurationQueryCommand command) {
        return Result.success(receiveDurationStatisticsService.getReceiveDurationTotal(command));
    }

    /**
     * 接收时长统计信息导出
     */
    @PostMapping("/download")
    @Operation(summary = "接收时长统计信息导出")
    public void downLoad(@RequestBody ReceiveDurationTotalResource resource, HttpServletResponse response) {
        receiveDurationStatisticsService.downLoad(resource.getRegionTimeMap(), resource.getUserTypeTimeMap(), response);
    }

}