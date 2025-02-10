package com.tecpie.platform.report.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.TaskReasonConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 创建时长统计分析 数据响应结果
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "创建时长统计分析 数据响应结果")
@Getter
@Setter
public class CreateDurationStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电任务通知Id
     */
    @Schema(description = "停电任务通知Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    @ExcelProperty(value = "通知单编号")
    private String taskCode;

    /**
     * 停电任务通知来源
     */
    @Schema(description = "停电任务通知来源")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Long taskSource;

    /**
     * 停电任务通知来源名称
     */
    @Schema(description = "停电任务通知来源名称")
    @JsonIgnore
    @ExcelProperty(value = "通知来源")
    private String taskSourceName;

    /**
     * 确认时间
     */
    @Schema(description = "确认时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "确认时间")
    @ColumnWidth(20)
    private LocalDateTime confirmTime;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停电时间")
    @ColumnWidth(20)
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "送电时间")
    @ColumnWidth(20)
    private LocalDateTime endTime;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    @Schema(description = "停电原因")
    @ExcelProperty(value = "停电原因", converter = TaskReasonConvert.class)
    private Integer reason;

    /**
     * 停电范围
     */
    @Schema(description = "停电范围")
    @ExcelProperty(value = "停电范围")
    private String ranges;

    /**
     * 停电户数
     */
    @Schema(description = "停电户数")
    @ExcelProperty(value = "停电户数")
    private Integer taskUserCount;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)")
    @ExcelProperty(value = "时长(天)")
    private Integer days;

    /**
     * 运方下达风险预警闯值
     */
    @Schema(description = "运方下达风险预警闯值")
    @ExcelIgnore
    private Integer alertDays;

}