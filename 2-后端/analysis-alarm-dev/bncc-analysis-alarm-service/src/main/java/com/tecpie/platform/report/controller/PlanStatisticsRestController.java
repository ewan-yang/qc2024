package com.tecpie.platform.report.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.report.api.command.query.PlanPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PlanQueryCommand;
import com.tecpie.platform.report.api.resource.PlanStatisticsResource;
import com.tecpie.platform.report.api.resource.PlanTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PlanStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PlanStatistics;
import com.tecpie.platform.report.service.PlanStatisticsService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 停电计划统计分析  控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/planStatistics")
@Tag(name = "停电计划统计分析接口定义")
public class PlanStatisticsRestController {

    private PlanStatisticsService planStatisticsService;

    @Autowired
    public void setPlanStatisticsRestController(PlanStatisticsService planStatisticsService) {
        this.planStatisticsService = planStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<PlanStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody PlanPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<PlanStatistics> entityList = planStatisticsService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, PlanStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-停电计划统计-导出
     * @param command 检查条件
     * @param response 文件流响应
     */
    @Operation(summary = "统计分析查询-停电计划统计-导出")
    @PostMapping("/export")
    public void planStatisticsExport(@RequestBody PlanPageQueryCommand command, HttpServletResponse response){
        planStatisticsService.planStatisticsExport(command, response);
    }

    /**
     * 停电计划统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "停电计划统计信息")
    public Result<PlanTotalResource> getCreateDurationTotal(@RequestBody PlanQueryCommand command) {
        return Result.success(planStatisticsService.getPlanTotal(command));
    }

}