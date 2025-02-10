package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.AlarmTypeConvert;
import com.tecpie.platform.notice.api.convert.TaskExecuteStatusConvert;
import com.tecpie.platform.notice.api.convert.TaskReasonConvert;
import com.tecpie.platform.notice.api.convert.TaskTypeConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运方下达风险预警 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "运方下达风险预警响应结果")
@Getter
@Setter
public class CarrierReleaseAdvanceNoticeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @Schema(description = "任务ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable taskId;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    @ExcelProperty(value = "通知单编号")
    private String taskCode;

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
     * 任务超期时长
     */
    @Schema(description = "任务超期时长")
    @ExcelProperty(value = "任务超期时长（天）")
    private Integer overdueDuration;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    @ExcelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 工作内容
     */
    @Schema(description = "工作内容")
    @ExcelProperty(value = "工作内容")
    private String jobContent;

    /**
     * 停电类型：1-计划停电
     */
    @Schema(description = "停电类型：1-计划停电")
    @ExcelProperty(value = "停电类型", converter = TaskTypeConvert.class)
    private Integer type;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    @Schema(description = "停电原因：1-设备检修，2-配合客户接入，3-配合市政过程")
    @ExcelProperty(value = "停电原因", converter = TaskReasonConvert.class)
    private Integer reason;

    /**
     * 停电范围
     */
    @Schema(description = "停电范围")
    @ExcelProperty(value = "停电范围")
    private String ranges;

    /**
     * 执行状态
     */
    @Schema(description = "执行状态")
    @ExcelProperty(value = "状态", converter = TaskExecuteStatusConvert.class)
    private String executeStatus;

    /**
     * 变电站名称
     */
    @Schema(description = "变电站名称")
    @ExcelIgnore
    private String stationName;

    /**
     * 线路名称
     */
    @Schema(description = "线路名称")
    @ExcelIgnore
    private String lineName;

    /**
     * 停电位置
     */
    @Schema(description = "停电位置")
    @ExcelIgnore
    private String location;

    /**
     * 未派发数量
     */
    @Schema(description = "未派发数量")
    @ExcelProperty(value = {"通知单派发情况", "未派发"})
    private Integer unassignedQty;

    /**
     * 已派发数量
     */
    @Schema(description = "已派发数量")
    @ExcelProperty(value = {"通知单派发情况", "已派发"})
    private Integer assignedQty;

    /**
     * 已取消数量
     */
    @Schema(description = "已取消数量")
    @ExcelProperty(value = {"通知单派发情况", "不派发"})
    private Integer cancelledQty;

    /**
     * 拒签数量
     */
    @Schema(description = "拒签数量")
    @ExcelProperty(value = {"用户反馈情况", "拒签"})
    private Integer rejectedQty;

    /**
     * 同意数量
     */
    @Schema(description = "同意数量")
    @ExcelProperty(value = {"用户反馈情况", "同意"})
    private Integer agreedQty;

    /**
     * 未签数量
     */
    @Schema(description = "未签数量")
    @ExcelProperty(value = {"用户反馈情况", "未签"})
    private Integer unsignedQty;

    /**
     * 版本
     */
    @Schema(description = "版本")
    @ExcelProperty(value = "版本")
    private Long version;

    /**
     * 告警类型
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    @ExcelProperty(value = "任务预警状态", converter = AlarmTypeConvert.class)
    private Integer alarmType;
}