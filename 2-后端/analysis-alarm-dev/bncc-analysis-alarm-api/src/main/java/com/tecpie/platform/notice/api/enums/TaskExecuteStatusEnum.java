package com.tecpie.platform.notice.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 任务执行状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/29 13:59
 */
@Getter
public enum TaskExecuteStatusEnum {

    // 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
    DTJ("110", "待提交"),
    DPF("120", "待派发"),
    ZXZ("130", "执行中"),
    YFK("140", "已反馈"),
    YQX("150", "已取消"),
    YWC("161", "已完成");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    TaskExecuteStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
