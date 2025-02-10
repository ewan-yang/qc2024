package com.tecpie.platform.user.business.data.api;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.data.api.resource.SysLovResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 字典表 表接口定义
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/30 0:33
 */
@Tag(name = "字典表接口定义")
@Headers("Accept: application/json")
public interface SysLovRestApi {

    /**
     * 根据Code获取字典数据
     */
    @Operation(summary = "根据Code获取字典数据")
    @RequestLine("GET /api/v1/lov/searchList/{lovCode}")
    Result<List<SysLovResource>> searchListByCode(@Param("lovCode") String lovCode);

}
