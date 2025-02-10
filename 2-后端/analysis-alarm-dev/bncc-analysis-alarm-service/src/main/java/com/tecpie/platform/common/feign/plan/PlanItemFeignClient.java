package com.tecpie.platform.common.feign.plan;

import com.tecpie.platform.plan_item.api.PlanItemRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 停电计划项Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "relay-task-service", contextId = "PlanItemFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface PlanItemFeignClient extends PlanItemRestApi {


}
