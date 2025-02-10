package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 任务通知用户反馈类型举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/29 13:59
 */
@Getter
public enum TaskUserFeedbackTypeEnum {

    // 停电任务通知用户类型：1-停电通知派发，2-停电通知取消
    ZC(1, "通知派发反馈"),
    QX(2, "取消通知派发反馈");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    TaskUserFeedbackTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
