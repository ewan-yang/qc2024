package com.tecpie.platform.notice.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 任务通知用户反馈状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/29 13:59
 */
@Getter
public enum TaskUserFeedbackStatusEnum {

    // 反馈状态：310-未签，320-同意，330-拒签
    WQ("310", "未签"),
    TY("320", "同意"),
    JQ("330", "拒签");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    TaskUserFeedbackStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean validValue(String value) {
        boolean flag = false;
        for (TaskUserFeedbackStatusEnum item : TaskUserFeedbackStatusEnum.values()) {
            if (item.getCode().equals(value)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
