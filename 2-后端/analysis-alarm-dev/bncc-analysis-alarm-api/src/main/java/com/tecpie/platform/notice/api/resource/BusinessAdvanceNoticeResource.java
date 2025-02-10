package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 业务预告警 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Schema(description = "业务预警响应结果")
public class BusinessAdvanceNoticeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电任务模块
     */
    @Schema(description = "业务类型")
    private Integer businessType;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    private Integer alarmType;

    /**
     * 告警标题
     */
    @Schema(description = "告警标题")
    private String alarmTitle;

    /**
     * 告警内容
     */
    @Schema(description = "告警内容")
    private String alarmContent;

    /**
     * 告警时间
     */
    @Schema(description = "告警时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime alarmTime;

    /**
     * 业务ID
     */
    @Schema(description = "业务ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable businessId;

    /**
     * 业务编号
     */
    @Schema(description = "业务编号")
    private String businessCode;

    /**
     * 业务内容
     */
    @Schema(description = "业务内容")
    private String businessContent;

    /**
     * 回执单号
     */
    @Schema(description = "回执单号")
    private String receiptCode;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime endTime;

}
