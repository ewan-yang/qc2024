package com.tecpie.platform.report.api.resource;


import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PowerCutRepeatDetailResource {

    @Schema(description = "停电通知id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 停电通知单编号
     */
    @Schema(description = "停电通知单编号")
    private String taskCode;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

}
