package com.tecpie.platform.plan.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.util.FileUtil;
import com.tecpie.platform.plan.api.PlanRestApi;
import com.tecpie.platform.plan.api.command.query.PlanPageQueryCommand;
import com.tecpie.platform.plan.api.command.query.PlanQueryCommand;
import com.tecpie.platform.plan.api.command.query.PlanStatisticsCommand;
import com.tecpie.platform.plan.api.command.save.PlanSaveCommand;
import com.tecpie.platform.plan.api.command.update.PlanMonthUpdateCommand;
import com.tecpie.platform.plan.api.command.update.PlanUpdateCommand;
import com.tecpie.platform.plan.api.resource.PlanResource;
import com.tecpie.platform.plan.api.resource.PlanStatisticsResource;
import com.tecpie.platform.plan.controller.assembler.command.save.PlanSaveCommandAssembler;
import com.tecpie.platform.plan.controller.assembler.command.update.PlanUpdateCommandAssembler;
import com.tecpie.platform.plan.controller.assembler.resource.PlanResourceAssembler;
import com.tecpie.platform.plan.entity.Plan;
import com.tecpie.platform.plan.service.PlanService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 停电计划表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/plan")
public class PlanRestController extends BaseController<PlanService, Plan, PlanResource> implements PlanRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<PlanResource> getPlan(@PathVariable("id") Serializable id) {
        Plan entity = this.baseService.selectExtensionById(id);
        return Result.success(PlanResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<PlanResource>> searchPlanList(@RequestBody PlanQueryCommand command) {
        List<Plan> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(PlanResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<PlanResource>> searchPlanPage(@RequestBody PlanPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(Plan.class, command));
        // 查询分页数据
        List<Plan> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, PlanResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存停电计划表数据
     */
    @Override
    @PostMapping
    public Result<Serializable> savePlan(@Valid @RequestBody PlanSaveCommand command) {
        return Result.success(this.baseService.savePlan(PlanSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新停电计划表数据
     */
    @Override
    @PutMapping("/{id}")
    public Result updatePlanById(@PathVariable("id") Serializable id, @Valid @RequestBody PlanUpdateCommand command) {
        this.baseService.updatePlan(id, PlanUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 更新停电计划月份、标题数据
     */
    @PutMapping("/updatePlanMonth/{id}")
    @Operation(summary = "更新停电计划月份、标题数据(更新)")
    public Result<Void> updatePlanMonthById(@PathVariable("id") Serializable id, @Valid @RequestBody PlanMonthUpdateCommand command) {
        this.baseService.updatePlanMonth(id, command.getPlanMonth(), command.getTitle());
        return Result.success();
    }

    /**
     * 根据ID删除停电计划表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result<Void> deletePlanById(@PathVariable("id") Serializable id) {
        baseService.deletePlanById(id);
        return Result.success();
    }

    /**
     * 下载停电计划导入模版
     */
    @Operation(summary = "下载停电计划导入模版")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/plan_template.xlsx");
            FileUtil.downloadFile(request, response, classPathResource.getFile(), false);
        } catch (Exception e) {
            log.error("下载停电计划导入模版异常", e);
        }
    }

    /**
     * 导入停电计划项信息
     */
    @PostMapping("/importPlan")
    @Operation(summary = "导入停电计划项信息")
    public Result<List<String>> importPlan(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(this.baseService.importPlan(file));
    }

    @PostMapping("/homePageStatistics")
    @Operation(summary = "首页-停电计划统计")
    public Result<PlanStatisticsResource> homePageStatistics(@RequestBody PlanStatisticsCommand planStatisticsCommand){
        return Result.success(this.baseService.homePageStatistics(planStatisticsCommand));
    }

    @Override
    protected PlanResource toResource(Plan entity) {
        return PlanResourceAssembler.INSTANCE.parse(entity);
    }

}