package com.tecpie.platform.engineers.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamPageQueryCommand;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamQueryCommand;
import com.tecpie.platform.engineers.api.command.save.EngineersTeamSaveCommand;
import com.tecpie.platform.engineers.api.command.update.EngineersTeamUpdateCommand;
import com.tecpie.platform.engineers.api.resource.EngineersTeamResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 工程队表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Tag(name = "工程队表接口定义")
@Headers("Accept: application/json")
public interface EngineersTeamRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<EngineersTeamResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/engineersTeam/{id}")
    Result<EngineersTeamResource> getEngineersTeam(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < EngineersTeamResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/engineersTeam/list")
    Result<List<EngineersTeamResource>> searchEngineersTeamList(EngineersTeamQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < EngineersTeamResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/engineersTeam/page")
    Result<PagedResult<EngineersTeamResource>> searchEngineersTeamPage(EngineersTeamPageQueryCommand command);

    /**
     * 保存工程队表数据
     *
     * @param command
     * @return Result<Serializable>
     */
    @Operation(summary = "保存工程队表数据")
    @RequestLine("POST /api/v1/engineersTeam")
    Result<Serializable> saveEngineersTeam(EngineersTeamSaveCommand command);

    /**
     * 根据ID更新工程队表数据
     *
     * @param command
     * @return Result
     */
    @Operation(summary = "更新工程队表数据")
    @RequestLine("PUT /api/v1/engineersTeam/{id}")
    Result updateEngineersTeamById(@Param("id") Serializable id, EngineersTeamUpdateCommand command);

    /**
     * 根据ID删除工程队表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除工程队表数据")
    @RequestLine("DELETE /api/v1/engineersTeam/{id}")
    Result deleteEngineersTeamById(@Param("id") Serializable id);

}