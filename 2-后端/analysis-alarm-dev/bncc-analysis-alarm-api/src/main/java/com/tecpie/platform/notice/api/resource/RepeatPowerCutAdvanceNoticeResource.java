package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.AlarmTypeConvert;
import com.tecpie.platform.notice.api.convert.Int2BooleanConvert;
import com.tecpie.platform.notice.api.convert.UserPriorityConvert;
import com.tecpie.platform.notice.api.convert.UserTypeConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 重复停电预警 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "重复停电预警响应结果")
@Getter
@Setter
public class RepeatPowerCutAdvanceNoticeResource implements Serializable {

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
     * 本次停电时间
     */
    @Schema(description = "本次停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "本次停电时间")
    private LocalDateTime thisPowerOutageTime;

    /**
     * 上次停电时间
     */
    @Schema(description = "上次停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "上次停电时间")
    private LocalDateTime lastPowerCutTime;

    /**
     * 停电间隔天数
     */
    @Schema(description = "停电间隔天数")
    @ExcelProperty(value = "停电间隔天数（天）")
    private Integer intervalDays;

    /**
     * 户号
     */
    @Schema(description = "户号")
    @ExcelProperty(value = "户号")
    private String accountNumber;

    /**
     * 电系编号
     */
    @Schema(description = "电系编号")
    @ExcelProperty(value = "电系编号")
    private String electricalNumber;

    /**
     * 电压等级
     */
    @Schema(description = "电压等级")
    @ExcelProperty(value = "电压等级")
    private String voltageLevel;

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
     * 是否短时停电：0-否，1-是
     */
    @Schema(description = "是否短时停电：0-否，1-是")
    @ExcelProperty(value = "是否短时停电", converter = Int2BooleanConvert.class)
    private Integer boolShortTime;

    /**
     * 用户重要性
     */
    @Schema(description = "用户重要性")
    @ExcelProperty(value = "用户重要性", converter = UserPriorityConvert.class)
    private Integer userPriority;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "所属台区")
    private String region;

    /**
     * 是否重复停电：0-否，1-是
     */
    @Schema(description = "是否重复停电：0-否，1-是")
    @ExcelIgnore
    private int boolRepeatPowerCut;

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
    @ExcelProperty(value = "关联通知单")
    private String taskCode;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime endTime;

    /**
     * 告警类型
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    @ExcelProperty(value = "重复停电预警",converter = AlarmTypeConvert.class)
    private Integer alarmType;

}