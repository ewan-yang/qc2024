package com.tecpie.platform.common.feign.task_user;

import com.tecpie.platform.task_user.api.TaskUserFeedbackLogRestApi;
import com.tecpie.starter.feign.support.configuration.FeignRequestLineConfiguration;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;

@Hidden
@FeignClient(name = "${eureka.service-prefix:}"
        + "relay-task-service", contextId = "TaskUserFeedbackLogFeignClient", configuration = FeignRequestLineConfiguration.class)
public interface TaskUserFeedbackLogFeignClient extends TaskUserFeedbackLogRestApi {
}
