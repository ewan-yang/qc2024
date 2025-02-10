package com.tecpie.platform.report.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.report.api.command.query.PowerCutRepeatPageQueryCommand;
import com.tecpie.platform.report.api.command.query.PowerCutRepeatQueryCommand;
import com.tecpie.platform.report.api.resource.PowerCutRepeatDetailResource;
import com.tecpie.platform.report.api.resource.PowerCutRepeatStatisticsResource;
import com.tecpie.platform.report.api.resource.PowerCutRepeatTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.PowerCutRepeatDetailResourceAssembler;
import com.tecpie.platform.report.controller.assembler.resource.PowerCutRepeatStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.PowerCutRepeatStatistics;
import com.tecpie.platform.report.service.PowerCutRepeatStatisticsService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 重复停电统计分析接口定义 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/powerCutRepeatStatistics")
@Tag(name = "重复停电统计分析接口定义")
public class PowerCutRepeatStatisticsRestController {

    private PowerCutRepeatStatisticsService powerCutRepeatStatisticsService;

    @Autowired
    public void setPowerCutRepeatStatisticsRestController(PowerCutRepeatStatisticsService powerCutRepeatStatisticsService) {
        this.powerCutRepeatStatisticsService = powerCutRepeatStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<PowerCutRepeatStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody PowerCutRepeatPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<PowerCutRepeatStatistics> entityList = powerCutRepeatStatisticsService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, PowerCutRepeatStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-重复停电统计-停电列表-导出
     */
    @PostMapping("/export")
    @Operation(summary = "统计分析查询-重复停电统计-停电列表-导出")
    public void powerCutRepeatStatisticsExport(@RequestBody PowerCutRepeatPageQueryCommand command,
                                               HttpServletResponse response){
        powerCutRepeatStatisticsService.powerCutRepeatStatisticsExport(command, response);
    }

    /**
     * 重复停电数据统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "重复停电数据统计信息")
    public Result<PowerCutRepeatTotalResource> getCreateDurationTotal(@RequestBody PowerCutRepeatQueryCommand command) {
        return Result.success(powerCutRepeatStatisticsService.getPowerCutRepeatTotal(command));
    }

    @PostMapping("/getDetailByRegion")
    @Operation(summary = "重复停电统计-查看详情")
    public Result<List<PowerCutRepeatDetailResource>> getDetailByRegion(@RequestBody PowerCutRepeatQueryCommand command){
        return Result.success(PowerCutRepeatDetailResourceAssembler.INSTANCE.parseList(powerCutRepeatStatisticsService.getDetailByRegion(command)));
    }

}