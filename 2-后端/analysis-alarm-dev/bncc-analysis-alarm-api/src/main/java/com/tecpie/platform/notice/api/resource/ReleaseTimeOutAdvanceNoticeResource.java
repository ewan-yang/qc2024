package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 下达超时预警 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "下达超时预警响应结果")
@Getter
@Setter
public class ReleaseTimeOutAdvanceNoticeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable taskUserId;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    @ExcelProperty(value = "回执单号")
    private String receiptCode;

    /**
     * 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
     */
    @Schema(description = "派发状态：210-未派发，220-已派发，230-已反馈，240-不派发")
    @ExcelProperty(value = "派发状态", converter = TaskAssignStatusConvert.class)
    private String assignStatus;

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
     * 距离停电时长
     */
    @Schema(description = "距离停电时长")
    @ExcelProperty(value = "距离停电时长（天）")
    private Integer distanceDuration;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
    @ExcelProperty(value = "反馈情况", converter = TaskFeedbackStatusConvert.class)
    private String feedbackStatus;

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
    @ExcelProperty(value = "客户名称")
    private String customerName;

    /**
     * 客户地址
     */
    @Schema(description = "客户地址")
    @ExcelProperty(value = "地址")
    private String customerAddress;

    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    @ExcelProperty(value = "联系方式")
    private String phone;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核")
    @ExcelProperty(value = "用户类型", converter = UserTypeConvert.class)
    private Integer userType;

    /**
     * 用户重要性
     */
    @Schema(description = "用户重要性")
    @ExcelProperty(value = "用户重要性", converter = UserPriorityConvert.class)
    private Integer userPriority;

    /**
     * 是否短时停电：0-否，1-是
     */
    @Schema(description = "是否短时停电：0-否，1-是")
    @ExcelProperty(value = "是否短时停电", converter = Int2BooleanConvert.class)
    private Integer boolShortTime;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "所属台区")
    private String region;

    /**
     * 反馈时间
     */
    @Schema(description = "反馈时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime feedbackTime;

    /**
     * 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
     */
    @Schema(description = "取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发")
    @ExcelIgnore
    private String cancelAssignStatus;

    /**
     * 取消通知反馈状态：510-未签，520-同意
     */
    @Schema(description = "取消通知反馈状态：510-未签，520-同意")
    @ExcelIgnore
    private String cancelFeedbackStatus;

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
    @ExcelProperty(value = "关联回执单")
    private String taskCode;

    /**
     * 告警类型
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    @ExcelProperty(value = "回执告警状态", converter = AlarmTypeConvert.class)
    private Integer alarmType;
}