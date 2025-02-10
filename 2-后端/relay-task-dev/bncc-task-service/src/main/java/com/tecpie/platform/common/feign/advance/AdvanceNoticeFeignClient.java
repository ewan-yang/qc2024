package com.tecpie.platform.common.feign.advance;

import com.tecpie.platform.notice.api.TaskAdvanceNoticeRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 告警通知Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "analysis-alarm-service", contextId = "AdvanceNoticeFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface AdvanceNoticeFeignClient extends TaskAdvanceNoticeRestApi {


}
