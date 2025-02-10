package com.tecpie.platform.report.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.report.api.command.query.ReleaseDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.ReleaseDurationQueryCommand;
import com.tecpie.platform.report.api.resource.ReleaseDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.ReleaseDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.ReleaseDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.ReleaseDurationStatistics;
import com.tecpie.platform.report.service.ReleaseDurationStatisticsService;
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
 * 下达时长统计分析 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/releaseDurationStatistics")
@Tag(name = "下达时长统计分析接口定义")
public class ReleaseDurationStatisticsRestController {

    private ReleaseDurationStatisticsService releaseDurationStatisticsService;

    @Autowired
    public void setReleaseDurationStatisticsRestController(ReleaseDurationStatisticsService releaseDurationStatisticsService) {
        this.releaseDurationStatisticsService = releaseDurationStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<ReleaseDurationStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody ReleaseDurationPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<ReleaseDurationStatistics> entityList = releaseDurationStatisticsService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, ReleaseDurationStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-下发时长统计-下发时长列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    @PostMapping("/export")
    @Operation(summary = "统计分析查询-下发时长统计-下发时长列表-导出")
    public void releaseDurationStatisticsExport(@RequestBody ReleaseDurationPageQueryCommand command,
                                                HttpServletResponse response){
        releaseDurationStatisticsService.releaseDurationStatisticsExport(command, response);
    }

    /**
     * 下达时长统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "下达时长统计信息")
    public Result<ReleaseDurationTotalResource> getCreateDurationTotal(@RequestBody ReleaseDurationQueryCommand command) {
        return Result.success(releaseDurationStatisticsService.getReleaseDurationTotal(command));
    }

    /**
     * 下发时长统计信息导出
     */
    @PostMapping("/download")
    @Operation(summary = "下发时长统计信息导出")
    public void downLoad(@RequestBody ReleaseDurationTotalResource resource, HttpServletResponse response) {
        releaseDurationStatisticsService.downLoad(resource.getRegionTimeMap(), resource.getUserTypeTimeMap(), response);
    }

}