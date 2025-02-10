package com.tecpie.platform.common.feign.advanceRule;

import com.tecpie.platform.rule.api.TaskAdvanceRuleRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 告警规则Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "analysis-alarm-service", contextId = "AdvanceRuleFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface AdvanceRuleFeignClient extends TaskAdvanceRuleRestApi {


}
