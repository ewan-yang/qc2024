package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 任务通知用户取消通知派发状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/13 13:59
 */
@Getter
public enum TaskUserCancelAssignStatusEnum {

    // 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
    WPF("410", "未派发"),
    YPF("420", "已派发"),
    YFK("430", "已反馈"),
    BPF("440", "不派发");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    TaskUserCancelAssignStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
