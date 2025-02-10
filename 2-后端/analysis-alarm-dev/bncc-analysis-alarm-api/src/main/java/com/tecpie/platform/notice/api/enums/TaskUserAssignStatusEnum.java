package com.tecpie.platform.notice.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 任务通知用户派发状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/29 13:59
 */
@Getter
public enum TaskUserAssignStatusEnum {

    // 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
    WPF("210", "未派发"),
    YPF("220", "已派发"),
    YFK("230", "已反馈"),
    BPF("240", "不派发");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    TaskUserAssignStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
