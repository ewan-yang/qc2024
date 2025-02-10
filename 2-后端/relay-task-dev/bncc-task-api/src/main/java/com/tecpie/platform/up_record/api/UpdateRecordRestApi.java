package com.tecpie.platform.up_record.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordPageQueryCommand;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordQueryCommand;
import com.tecpie.platform.up_record.api.resource.UpdateRecordResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 数据修改记录表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Tag(name = "数据修改记录表接口定义")
@Headers("Accept: application/json")
public interface UpdateRecordRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<UpdateRecordResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/updateRecord/{id}")
    Result<UpdateRecordResource> getUpdateRecord(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < UpdateRecordResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/updateRecord/list")
    Result<List<UpdateRecordResource>> searchUpdateRecordList(UpdateRecordQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < UpdateRecordResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/updateRecord/page")
    Result<PagedResult<UpdateRecordResource>> searchUpdateRecordPage(UpdateRecordPageQueryCommand command);


}