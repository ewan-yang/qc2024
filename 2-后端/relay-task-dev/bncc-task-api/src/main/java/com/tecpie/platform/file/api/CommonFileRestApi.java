package com.tecpie.platform.file.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.file.api.command.query.CommonFilePageQueryCommand;
import com.tecpie.platform.file.api.command.query.CommonFileQueryCommand;
import com.tecpie.platform.file.api.resource.CommonFileResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 文件和图片表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Tag(name = "文件和图片表接口定义")
@Headers("Accept: application/json")
public interface CommonFileRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<CommonFileResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/commonFile/{id}")
    Result<CommonFileResource> getCommonFile(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < CommonFileResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/commonFile/list")
    Result<List<CommonFileResource>> searchCommonFileList(CommonFileQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < CommonFileResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/commonFile/page")
    Result<PagedResult<CommonFileResource>> searchCommonFilePage(CommonFilePageQueryCommand command);

    /**
     * 根据ID删除文件和图片表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除文件和图片表数据")
    @RequestLine("DELETE /api/v1/commonFile/{id}")
    Result deleteCommonFileById(@Param("id") Serializable id);

}