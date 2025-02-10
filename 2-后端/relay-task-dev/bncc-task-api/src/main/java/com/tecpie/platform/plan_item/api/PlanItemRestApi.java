package com.tecpie.platform.plan_item.api;

import com.tecpie.platform.plan_item.api.command.save.PlanItemSaveCommand;
import com.tecpie.platform.plan_item.api.command.update.PlanItemUpdateCommand;
import com.tecpie.platform.plan_item.api.command.query.PlanItemQueryCommand;
import com.tecpie.platform.plan_item.api.command.query.PlanItemPageQueryCommand;
import com.tecpie.platform.plan_item.api.resource.PlanItemResource;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 停电计划项表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Tag(name = "停电计划项表接口定义")
@Headers("Accept: application/json")
public interface PlanItemRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<PlanItemResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/planItem/{id}")
    Result<PlanItemResource> getPlanItem(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < PlanItemResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/planItem/list")
    Result<List<PlanItemResource>> searchPlanItemList(PlanItemQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < PlanItemResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/planItem/page")
    Result<PagedResult<PlanItemResource>> searchPlanItemPage(PlanItemPageQueryCommand command);

    /**
     * 根据ID删除停电计划项表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除停电计划项表数据")
    @RequestLine("DELETE /api/v1/planItem/{id}")
    Result deletePlanItemById(@Param("id") Serializable id);

}