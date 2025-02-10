package com.tecpie.platform.report.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.report.api.command.query.TaskChangePageQueryCommand;
import com.tecpie.platform.report.api.command.query.TaskChangeQueryCommand;
import com.tecpie.platform.report.api.resource.TaskChangeStatisticsResource;
import com.tecpie.platform.report.api.resource.TaskChangeTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.TaskChangeStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.TaskChangeStatistics;
import com.tecpie.platform.report.service.TaskChangeStatisticsService;
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
 * 通知变更统计分析统计 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskChangeStatistics")
@Tag(name = "通知变更统计分析接口定义")
public class TaskChangeStatisticsRestController {

    private TaskChangeStatisticsService taskChangeStatisticsService;

    @Autowired
    public void setTaskChangeStatisticsRestController(TaskChangeStatisticsService taskChangeStatisticsService) {
        this.taskChangeStatisticsService = taskChangeStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<TaskChangeStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody TaskChangePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<TaskChangeStatistics> entityList = taskChangeStatisticsService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskChangeStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-通知变更统计-通知变更列表-导出
     *
     * @param command  命令
     * @param response 响应
     */
    @PostMapping("/export")
    @Operation(summary = "统计分析查询-通知变更统计-通知变更列表-导出")
    public void taskChangeStatisticsExport(@RequestBody TaskChangePageQueryCommand command, HttpServletResponse response){
        taskChangeStatisticsService.taskChangeStatisticsExport(command, response);
    }

    /**
     * 通知变更统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "通知变更统计信息")
    public Result<TaskChangeTotalResource> getCreateDurationTotal(@RequestBody TaskChangeQueryCommand command) {
        return Result.success(taskChangeStatisticsService.getTaskChangeTotal(command));
    }

}