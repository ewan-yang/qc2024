package com.tecpie.platform.common.feign.task;

import com.tecpie.platform.task_user.api.TaskUserRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 停电任务通知用户Api
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 13:43
 */
@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "relay-task-service", contextId = "TaskUserFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface TaskUserFeignClient extends TaskUserRestApi {


}
