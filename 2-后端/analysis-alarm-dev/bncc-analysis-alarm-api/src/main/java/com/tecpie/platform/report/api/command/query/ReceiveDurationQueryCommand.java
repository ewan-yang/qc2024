package com.tecpie.platform.report.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 接收时长 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "接收时长 检索请求参数")
@Getter
@Setter
public class ReceiveDurationQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知单编号
     */
    @Schema(description = "通知单编号")
    private String taskCode;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    private String region;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private Integer userType;

    /**
     * 反馈时间 起始
     */
    @Schema(description = "反馈时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate feedbackTimeBegin;

    /**
     * 反馈时间  截止
     */
    @Schema(description = "反馈时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate feedbackTimeEnd;

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
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.getFeedbackTimeBegin() == null) {
            this.setFeedbackTimeBegin(LocalDate.now().plusMonths(-1));
        }
        LocalDate currDateLocal = LocalDate.now();
        if (this.getFeedbackTimeEnd() != null) {
            currDateLocal = this.getFeedbackTimeEnd();
        }
        this.setFeedbackTimeEnd(currDateLocal.plusDays(1));
        // 停电时间
        if (this.getStartDateEnd() != null) {
            this.setStartDateEnd(this.getStartDateEnd().plusDays(1));
        }
    }

}