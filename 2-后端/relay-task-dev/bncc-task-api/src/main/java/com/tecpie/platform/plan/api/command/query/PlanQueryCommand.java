package com.tecpie.platform.plan.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 停电计划表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划表分页检索请求参数")
@Getter
@Setter
public class PlanQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划编号
     */
    @Schema(description = "计划编号")
    private String code;

    /**
     * 计划年月
     */
    @Schema(description = "计划年月")
    private String planMonth;

    /**
     * 计划标题
     */
    @Schema(description = "计划标题")
    private String title;

    /**
     * 创建开始时间
     */
    @Schema(description = "创建开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateBegin;

    /**
     * 创建结束时间
     */
    @Schema(description = "创建结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateEnd;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.createDateEnd != null) {
            this.createDateEnd = this.createDateEnd.plusDays(1);
        }
    }
}