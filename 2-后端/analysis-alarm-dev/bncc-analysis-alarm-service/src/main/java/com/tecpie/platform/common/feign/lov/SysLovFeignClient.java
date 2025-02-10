package com.tecpie.platform.common.feign.lov;

import com.tecpie.platform.user.business.data.api.SysLovRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 系统字典Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "relay-task-service", contextId = "SysLovFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface SysLovFeignClient extends SysLovRestApi {


}
