package com.tecpie.platform.report.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.TaskReasonConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电统计分析 数据响应结果
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "停电统计分析 数据响应结果")
@Getter
@Setter
public class PowerCutStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "台区")
    private String region;

    /**
     * 停电任务通知表ID
     */
    @Schema(description = "停电任务通知表ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable taskId;

    /**
     * 所属台区用户数量
     */
    @Schema(description = "所属台区用户数量")
    @ExcelProperty(value = "台区户数")
    private int regionUserCount;

    /**
     * 变电站名称
     */
    @Schema(description = "变电站名称")
    @ExcelProperty(value = "变电站")
    private String stationName;

    /**
     * 线路名称
     */
    @Schema(description = "线路名称")
    @ExcelProperty(value = "线路名称")
    private String lineName;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    @ExcelProperty(value = "关联通知单")
    private String taskCode;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停电时间")
    @ColumnWidth(20)
    private LocalDateTime startTime;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    @Schema(description = "停电原因")
    @ExcelProperty(value = "停电原因", converter = TaskReasonConvert.class)
    private Integer reason;

}