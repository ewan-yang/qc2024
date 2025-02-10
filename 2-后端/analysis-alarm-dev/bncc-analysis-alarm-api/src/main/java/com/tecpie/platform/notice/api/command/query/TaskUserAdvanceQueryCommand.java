package com.tecpie.platform.notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户拒签、下达超时、重复停电预警 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "用户拒签、下达超时、重复停电预警检索请求参数")
@Getter
@Setter
public class TaskUserAdvanceQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电时间 起始
     */
    @Schema(description = "停电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateBegin;

    /**
     * 停电时间  截止
     */
    @Schema(description = "停电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateEnd;

    /**
     * 送电时间 起始
     */
    @Schema(description = "送电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate endDateBegin;

    /**
     * 送电时间 截止
     */
    @Schema(description = "送电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate endDateEnd;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    private String receiptCode;

    /**
     * 通知单编号
     */
    @Schema(description = "通知单编号")
    private String taskCode;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startDateEnd != null) {
            this.startDateEnd = this.startDateEnd.plusDays(1);
        }
        if (this.endDateEnd != null) {
            this.endDateEnd = this.endDateEnd.plusDays(1);
        }
        if (this.startDateBegin == null) {
            this.startDateBegin = LocalDate.now();
        }
    }

}