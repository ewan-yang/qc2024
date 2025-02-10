package com.tecpie.platform.plan.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 首页-停电计划统计-请求参数
 */
@Schema(description = "首页-停电计划统计-请求参数")
@Setter
@Getter
public class PlanStatisticsCommand {

    @Schema(description = "开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateBegin;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateEnd;

}
