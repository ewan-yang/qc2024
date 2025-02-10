package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 是否  枚举
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/1 15:09
 */
@Getter
public enum YesOrNoEnum {

    // 未配置项目审批流程
    NO(0, "否"),
    YES(1, "是");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    YesOrNoEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
