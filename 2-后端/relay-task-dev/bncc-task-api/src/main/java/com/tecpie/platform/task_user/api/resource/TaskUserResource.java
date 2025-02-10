package com.tecpie.platform.task_user.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.task.api.resource.TaskResource;
import com.tecpie.platform.task_user.api.resource.excel.convert.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务用户表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户表响应结果")
@Getter
@Setter
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class TaskUserResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Serializable id;

    /**
     * 停电任务通知表ID，与t_task表关联
     */
    @Schema(description = "停电任务通知表ID，与t_task表关联")
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Serializable taskId;

    /**
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    @Schema(description = "用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核")
    @ExcelProperty(value = "用户类型", converter = UserTypeConvert.class)
    private Integer userType;

    /**
     * 地址
     */
    @Schema(description = "地址")
    @ExcelProperty(value = "地址")
    private String address;

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
    @ExcelProperty(value = "客户地址")
    private String customerAddress;

    /**
     * 站线名称
     */
    @Schema(description = "站线名称")
    @ExcelProperty(value = "站线名称")
    private String stationLineName;

    /**
     * 用户重要性：字典未定义
     */
    @Schema(description = "用户重要性：字典未定义")
    @ExcelProperty(value = "用户重要性", converter = UserPriorityConvert.class)
    private Integer userPriority;

    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    @ExcelProperty(value = "联系方式")
    private String phone;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    @ExcelProperty(value = "回执编号")
    private String receiptCode;

    /**
     * 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
     */
    @Schema(description = "派发状态：210-未派发，220-已派发，230-已反馈，240-不派发")
    @ExcelProperty(value = "停电通知派发状态", converter = AssignStatusConvert.class)
    private String assignStatus;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
    @ExcelProperty(value = "停电通知反馈情况", converter = FeedbackStatusConvert.class)
    private String feedbackStatus;

    /**
     * 工程队名称
     */
    @Schema(description = "工程队名称")
    @ExcelProperty(value = "派发负责方")
    private String engineersTeamName;

    /**
     * 是否短时停电：0-否，1-是
     */
    @Schema(description = "是否短时停电：0-否，1-是")
    @ExcelProperty(value = "是否短时停电", converter = Num2BoolConvert.class)
    private Integer boolShortTime;


    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "所属台区")
    private String region;

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
     * 是否重复停电：0-否，1-是
     */
    @Schema(description = "是否重复停电：0-否，1-是")
    @ExcelProperty(value = "是否重复停电", converter = Num2BoolConvert.class)
    private Integer boolRepeatPowerCut;

    /**
     * 派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @Schema(description = "派发方式：1-送达现场，2-微信通知，3-营销通知")
    @ExcelProperty(value = "停电通知派发方式", converter = AssignMethodConvert.class)
    private Integer assignMethod;

    /**
     * 反馈时间
     */
    @Schema(description = "反馈时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停电通知反馈时间")
    private LocalDateTime feedbackTime;

    /**
     * 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
     */
    @Schema(description = "取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发")
    @ExcelProperty(value = "取消通知派发状态", converter = CancelAssignStatusConvert.class)
    private String cancelAssignStatus;

    /**
     * 取消通知派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @Schema(description = "取消通知派发方式：1-送达现场，2-微信通知，3-营销通知")
    @ExcelProperty(value = "取消通知派发方式", converter = AssignMethodConvert.class)
    private Integer cancelAssignMethod;

    /**
     * 取消通知反馈状态：510-未签，520-同意
     */
    @Schema(description = "取消通知反馈状态：510-未签，520-同意")
    @ExcelProperty(value = "取消通知反馈状态", converter = CancelFeedbackStatusConvert.class)
    private String cancelFeedbackStatus;

    /**
     * 取消通知反馈时间
     */
    @Schema(description = "取消通知反馈时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "取消通知反馈时间")
    private LocalDateTime cancelFeedbackTime;

    /**
     * 所属接入点
     */
    @Schema(description = "所属接入点")
    @ExcelProperty(value = "所属接入点")
    private String accessPoint;

    /**
     * 所属接入点名称
     */
    @Schema(description = "所属接入点名称")
    @ExcelProperty(value = "所属接入点名称")
    private String accessPointName;

    /**
     * 装接容量
     */
    @Schema(description = "装接容量")
    @ExcelProperty(value = "装接容量")
    private String capacity;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 邮编
     */
    @Schema(description = "邮编")
    @ExcelProperty(value = "邮编")
    private String postCode;

    /**
     * 上次停电时间
     */
    @Schema(description = "上次停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime lastPowerCutTime;


    /**
     * 派发时间
     */
    @Schema(description = "派发时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime assignTime;

    /**
     * 派发负责方(工程队id)
     */
    @Schema(description = "派发负责方(工程队id)")
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Serializable engineersTeamId;

    /**
     * 取消原因
     */
    @Schema(description = "取消原因")
    @ExcelIgnore
    private String cancelReason;

    /**
     * 新的联系方式
     */
    @Schema(description = "新的联系方式")
    @ExcelIgnore
    private String backupPhone;

    /**
     * 拒签理由
     */
    @Schema(description = "拒签理由")
    @ExcelIgnore
    private String rejectedReason;

    /**
     * 扩展属性1
     */
    @Schema(description = "扩展属性1")
    @ExcelIgnore
    private String attribute1;

    /**
     * 扩展属性2
     */
    @Schema(description = "扩展属性2")
    @ExcelIgnore
    private String attribute2;

    /**
     * 扩展属性3
     */
    @Schema(description = "扩展属性3")
    @ExcelIgnore
    private String attribute3;

    /**
     * 超期状态
     */
    @Schema(description = "超期状态")
    @ExcelIgnore
    private String overdueStatus;

    /**
     * 状态：0-停用，1-启用
     */
    @Schema(description = "状态：0-停用，1-启用")
    @ExcelIgnore
    private Integer status;


    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Serializable createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Serializable updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime updateDate;

    /**
     * 停电任务对象
     */
    @Schema(description = "停电任务对象")
    @ExcelIgnore
    private TaskResource task;

    /**
     * 停电间隔天数
     */
    @Schema(description = "停电间隔天数")
    @ExcelIgnore
    private int intervalDays;

    /**
     * 距离停电时长
     */
    @Schema(description = "距离停电时长")
    @ExcelIgnore
    private int distanceDuration;

}