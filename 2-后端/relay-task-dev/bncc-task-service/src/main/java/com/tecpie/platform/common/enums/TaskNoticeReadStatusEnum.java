package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 通知读取状态举类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/13 13:59
 */
@Getter
public enum TaskNoticeReadStatusEnum {

    // 通知读取状态：0-未读，1-已读
    NO(0, "未读"),
    YES(1, "已读");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    TaskNoticeReadStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
