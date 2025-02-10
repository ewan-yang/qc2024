package com.tecpie.platform.common.controller;

import com.tecpie.library.common.business.controller.resource.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/21 11:09
 */
@RestController
@RequestMapping("/analyse-warning/api/v1/is_health")
public class HealthCheckController {

    @GetMapping()
    public Result<Void> isHealth() {
        return new Result<>();
    }

}
