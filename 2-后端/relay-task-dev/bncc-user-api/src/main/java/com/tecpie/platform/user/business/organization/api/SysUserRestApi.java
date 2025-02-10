package com.tecpie.platform.user.business.organization.api;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserResource;
import feign.Headers;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 系统用户表接口定义
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/30 0:33
 */
@Tag(name = "系统用户表接口定义")
@Headers("Accept: application/json")
public interface SysUserRestApi {

    /**
     * 根据ids获取用户信息
     */
    @Operation(summary = "根据ids获取用户信息")
    @RequestLine("POST /api/v1/user/getUserListByIds")
    Result<List<SysUserResource>> getUserListByIds(@RequestBody SysUserQueryCommand command);

}
