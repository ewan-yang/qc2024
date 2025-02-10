package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 告警类型 枚举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/21 13:59
 */
@Getter
public enum AlarmTypeEnum {

    // 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
    ZC(0, "正常"),
    XD_CH(1, "下达超时告警"),
    XD_FX(2, "运方下达风险告警"),
    YH_JQ(3, "用户拒签告警"),
    CH_TD(4, "重复停电预警"),
    JH_BG(5, "计划变更预警");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    AlarmTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
