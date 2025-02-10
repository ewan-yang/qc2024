package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 停电计划项执行状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/13 13:59
 */
@Getter
public enum PlanItemExecuteStatusEnum {

    // 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
    WGL("010", "未关联"),
//    DZX("010", "待执行"),
    YGL("020", "已关联"),
//    ZXZ("020", "执行中"),
    YWC("031", "已完成"),
    YQX("032", "已取消"),
    WZX("040", "未执行");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    PlanItemExecuteStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
