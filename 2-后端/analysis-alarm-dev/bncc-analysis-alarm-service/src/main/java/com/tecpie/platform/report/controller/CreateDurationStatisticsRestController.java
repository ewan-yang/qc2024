package com.tecpie.platform.report.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.enums.AlarmTypeEnum;
import com.tecpie.platform.report.api.command.query.CreateDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.CreateDurationQueryCommand;
import com.tecpie.platform.report.api.resource.CreateDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.CreateDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.CreateDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.CreateDurationStatistics;
import com.tecpie.platform.report.service.CreateDurationStatisticsService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.service.TaskAdvanceRuleService;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 创建时长分析 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/createDurationStatistics")
@Tag(name = "创建时长统计分析接口定义")
public class CreateDurationStatisticsRestController {

    private CreateDurationStatisticsService createDurationStatisticsService;

    @Autowired
    private TaskAdvanceRuleService taskAdvanceRuleService;

    @Autowired
    public void setCreateDurationStatisticsRestController(CreateDurationStatisticsService createDurationStatisticsService) {
        this.createDurationStatisticsService = createDurationStatisticsService;
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<CreateDurationStatisticsResource>> searchTaskAdvanceRulePage(@RequestBody CreateDurationPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<CreateDurationStatistics> entityList = createDurationStatisticsService.searchExtensionPageList(command.getCondition());
        // 查询下达风险预警值
        TaskAdvanceRule advanceRule = taskAdvanceRuleService.lambdaQuery().eq(TaskAdvanceRule::getRuleCode, "GZ0000001").one();
        if (ObjectUtil.isNotNull(advanceRule) && StringUtils.isNotBlank(advanceRule.getRuleCode())) {
            entityList.forEach(t -> t.setAlertDays(advanceRule.getDays()));
        }
        // 构造分页结果
        return Result.success(entityList, CreateDurationStatisticsResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 统计分析查询-创建时长统计-创建时长列表-导出
     */
    @Operation(summary = "统计分析查询-创建时长统计-创建时长列表-导出")
    @PostMapping("/export")
    public void createDurationStatisticsExport(@RequestBody CreateDurationPageQueryCommand command,
                                               HttpServletResponse response) {
        createDurationStatisticsService.createDurationStatisticsExport(command, response);
    }

    /**
     * 创建时长统计信息
     */
    @PostMapping("/total")
    @Operation(summary = "创建时长统计信息")
    public Result<CreateDurationTotalResource> getCreateDurationTotal(@RequestBody CreateDurationQueryCommand command) {
        return Result.success(createDurationStatisticsService.getCreateDurationTotal(command));
    }

}