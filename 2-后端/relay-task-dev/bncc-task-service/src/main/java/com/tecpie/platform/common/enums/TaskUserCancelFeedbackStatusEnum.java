package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
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
public enum TaskUserCancelFeedbackStatusEnum {

    // 取消通知反馈状态：510-未签，520-同意
    WQ("510", "未签"),
    TY("520", "同意"),
    JQ("530", "拒签(取消任务没有拒签，这里为了更加保险判断使用)");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    TaskUserCancelFeedbackStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean validValue(String value) {
        boolean flag = false;
        for (TaskUserCancelFeedbackStatusEnum item : TaskUserCancelFeedbackStatusEnum.values()) {
            if ("530".equals(value)) {
                throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, "已取消的任务不能进行拒签操作");
            }
            if (item.getCode().equals(value)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
