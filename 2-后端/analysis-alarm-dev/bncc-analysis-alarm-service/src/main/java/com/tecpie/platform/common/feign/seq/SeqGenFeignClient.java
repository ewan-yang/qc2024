package com.tecpie.platform.common.feign.seq;

import com.tecpie.platform.seq.api.SeqGenRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 序列生成Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "relay-task-service", contextId = "SeqGenFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface SeqGenFeignClient extends SeqGenRestApi {


}
