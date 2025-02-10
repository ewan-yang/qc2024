package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 业务类型 枚举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/21 13:59
 */
@Getter
public enum BusinessTypeEnum {

    // 业务类型：1-停电任务模块，2-停电任务通知用户模块，3-停电计划模块
    TASK_MODEL(1, "停电任务通知模块"),
    TASK_USER_MODEL(2, "停电任务通知用户模块"),
    PLAN_MODEL(3, "停电计划项模块");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    BusinessTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
