package com.tecpie.platform.report.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.report.api.command.query.PowerCutPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutStatisticsResource;
import com.tecpie.platform.report.api.resource.PowerCutTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PowerCutStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PowerCutStatistics;
import com.tecpie.platform.report.service.PowerCutStatisticsService;
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
 * 停电统计分析  控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/powerCutStatistics")
@Tag(name = "停电统计分析接口定义")
public class PowerCutStatisticsRestController {

    private PowerCutStatisticsService powerCutStatisticsService;

    @Autowired
    public void setPowerCutStatisticsRestController(PowerCutStatisticsService powerCutStatisticsService) {
        this.powerCutStatisticsService = powerCutStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<PowerCutStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody PowerCutPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<PowerCutStatistics> entityList = powerCutStatisticsService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, PowerCutStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-停电统计-停电列表-导出
     */
    @PostMapping("/export")
    @Operation(summary = "统计分析查询-停电统计-停电列表-导出")
    public void powerCutStatisticsExport(@RequestBody PowerCutPageQueryCommand command, HttpServletResponse response) {
        powerCutStatisticsService.powerCutStatisticsExport(command, response);
    }

    /**
     * 停电统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "停电统计信息")
    public Result<PowerCutTotalResource> getCreateDurationTotal(@RequestBody PowerCutQueryCommand command) {
        return Result.success(powerCutStatisticsService.getPowerCutTotal(command));
    }

}