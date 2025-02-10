package com.tecpie.platform.report.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.UserTypeConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接收时长统计分析 数据响应结果
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "接收时长统计分析 数据响应结果")
@Getter
@Setter
public class ReceiveDurationStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 停电任务通知表ID
     */
    @Schema(description = "停电任务通知表ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable taskId;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    @ExcelProperty(value = "回执单号")
    private String receiptCode;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停电时间")
    @ColumnWidth(20)
    private LocalDateTime startTime;

    /**
     * 派发时间
     */
    @Schema(description = "派发时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "派发时间")
    @ColumnWidth(20)
    private LocalDateTime assignTime;

    /**
     * 户号
     */
    @Schema(description = "户号")
    @ExcelProperty(value = "户号")
    private String accountNumber;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称")
    @ExcelProperty(value = "用户名称")
    private String customerName;

    /**
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    @Schema(description = "用户类型")
    @ExcelProperty(value = "用户类型", converter = UserTypeConvert.class)
    private Integer userType;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "所属台区")
    private String region;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    @ExcelProperty(value = "关联通知单")
    private String taskCode;

    /**
     * 反馈时间
     */
    @Schema(description = "反馈时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "反馈时间")
    @ColumnWidth(20)
    private LocalDateTime feedbackTime;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)")
    @ExcelProperty(value = "时长（天）")
    private Integer hourDays;

    /**
     * 下达超时警告阈值（天）
     */
    @Schema(description = "下达超时警告阈值（天）")
    @ExcelIgnore
    private Integer alertDays;

}