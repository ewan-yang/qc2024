package com.tecpie.platform.rule.api;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import feign.Headers;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

/**
 * 告警规则维护 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Tag(name = "告警规则维护接口定义")
@Headers("Accept: application/json")
public interface TaskAdvanceRuleRestApi {

    /**
     * 获取下达风险预警规则编号
     *
     * @return Result<TaskAdvanceRuleResource>
     */
    @Operation(summary = "获取下达风险预警规则编号")
    @RequestLine("GET /api/v1/taskAdvanceRule/getCarrierReleaseRule")
    Result<TaskAdvanceRuleResource> getCarrierReleaseRule();

    /**
     * 获取下达超时预警规则编号
     *
     * @return Result<TaskAdvanceRuleResource>
     */
    @Operation(summary = "获取下达超时预警规则编号")
    @RequestLine("GET /api/v1/taskAdvanceRule/getReleaseTimeOutRule")
    Result<TaskAdvanceRuleResource> getReleaseTimeOutRule();

    /**
     * 获取反馈超期预警规则
     *
     * @return Result<TaskAdvanceRuleResource>
     */
    @Operation(summary = "获取反馈超期预警规则")
    @RequestLine("GET /api/v1/taskAdvanceRule/getFeedbackTimeOutRule")
    Result<TaskAdvanceRuleResource> getFeedbackTimeOutRule();

    /**
     * 获取重复停电预警规则编号
     *
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "获取重复停电预警规则编号")
    @RequestLine("GET /api/v1/taskAdvanceRule/getRepeatPowerCutRule")
    Result<Map<String, Integer>> getRepeatPowerCutRule();


}