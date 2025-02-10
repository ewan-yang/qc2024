package com.tecpie.platform.seq.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.seq.api.command.query.SeqGenPageQueryCommand;
import com.tecpie.platform.seq.api.command.query.SeqGenQueryCommand;
import com.tecpie.platform.seq.api.command.save.SeqGenSaveCommand;
import com.tecpie.platform.seq.api.command.update.SeqGenUpdateCommand;
import com.tecpie.platform.seq.api.resource.SeqGenResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 序列表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Tag(name = "序列表接口定义")
@Headers("Accept: application/json")
public interface SeqGenRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<SeqGenResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/seqGen/{id}")
    Result<SeqGenResource> getSeqGen(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < SeqGenResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/seqGen/list")
    Result<List<SeqGenResource>> searchSeqGenList(SeqGenQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < SeqGenResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/seqGen/page")
    Result<PagedResult<SeqGenResource>> searchSeqGenPage(SeqGenPageQueryCommand command);

    /**
     * 根据业务类型获取序列数据
     *
     * @param businessType 业务类型
     * @return Result<List < SeqGenResource>>
     */
    @Operation(summary = "根据业务类型获取序列数据")
    @RequestLine("POST /api/v1/seqGen/getData/{businessType}")
    Result<SeqGenResource> getSeqByBusinessType(@Param("businessType") String businessType);

    /**
     * 根据业务类型获取序列值
     *
     * @param businessType 业务类型
     * @return Result<List < SeqGenResource>>
     */
    @Operation(summary = "根据业务类型获取序列值")
    @RequestLine("POST /api/v1/seqGen/getValue/{businessType}")
    Result<String> getSeqValue(@Param("businessType") String businessType);

    /**
     * 保存序列表数据
     *
     * @param command
     * @return Result<Serializable>
     */
    @Operation(summary = "保存序列表数据")
    @RequestLine("POST /api/v1/seqGen")
    Result<Serializable> saveSeqGen(SeqGenSaveCommand command);

    /**
     * 根据ID更新序列表数据
     *
     * @param command
     * @return Result
     */
    @Operation(summary = "更新序列表数据")
    @RequestLine("PUT /api/v1/seqGen/{id}")
    Result updateSeqGenById(@Param("id") Serializable id, SeqGenUpdateCommand command);

    /**
     * 根据ID删除序列表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除序列表数据")
    @RequestLine("DELETE /api/v1/seqGen/{id}")
    Result deleteSeqGenById(@Param("id") Serializable id);

    /**
     * 根据ID变更序列表状态
     *
     * @param id
     * @param status
     * @return String
     */
    @Operation(summary = "变更序列表状态")
    @RequestLine("PUT /api/v1/seqGen/{id}/status/{status}")
    Result changeSeqGenStatusById(@Param("id") Serializable id, @Param("status") Integer status);

}