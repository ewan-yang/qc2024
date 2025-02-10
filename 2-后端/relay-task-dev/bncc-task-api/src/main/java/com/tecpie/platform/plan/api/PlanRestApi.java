package com.tecpie.platform.plan.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.plan.api.command.query.PlanPageQueryCommand;
import com.tecpie.platform.plan.api.command.query.PlanQueryCommand;
import com.tecpie.platform.plan.api.command.save.PlanSaveCommand;
import com.tecpie.platform.plan.api.command.update.PlanUpdateCommand;
import com.tecpie.platform.plan.api.resource.PlanResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电计划表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Tag(name = "停电计划表接口定义")
@Headers("Accept: application/json")
public interface PlanRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<PlanResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/plan/{id}")
    Result<PlanResource> getPlan(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < PlanResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/plan/list")
    Result<List<PlanResource>> searchPlanList(PlanQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < PlanResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/plan/page")
    Result<PagedResult<PlanResource>> searchPlanPage(PlanPageQueryCommand command);

    /**
     * 保存停电计划表数据
     *
     * @param command
     * @return Result<Serializable>
     */
    @Operation(summary = "保存停电计划表数据")
    @RequestLine("POST /api/v1/plan")
    Result<Serializable> savePlan(PlanSaveCommand command);

    /**
     * 根据ID更新停电计划表数据
     *
     * @param command
     * @return Result
     */
    @Operation(summary = "更新停电计划表数据")
    @RequestLine("PUT /api/v1/plan/{id}")
    Result updatePlanById(@Param("id") Serializable id, PlanUpdateCommand command);

    /**
     * 根据ID删除停电计划表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除停电计划表数据")
    @RequestLine("DELETE /api/v1/plan/{id}")
    Result deletePlanById(@Param("id") Serializable id);

}