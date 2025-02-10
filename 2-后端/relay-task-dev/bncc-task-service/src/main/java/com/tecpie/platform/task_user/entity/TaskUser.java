package com.tecpie.platform.task_user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.task.entity.Task;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务用户表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task_user")
public class TaskUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电任务通知表ID，与t_task表关联
     */
    @TableField(value = "task_id")
    private Serializable taskId;

    /**
     * 回执编号
     */
    @TableField(value = "receipt_code")
    private String receiptCode;

    /**
     * 户号
     */
    @TableField(value = "account_number")
    private String accountNumber;

    /**
     * 客户名称
     */
    @TableField(value = "customer_name")
    private String customerName;

    /**
     * 客户地址
     */
    @TableField(value = "customer_address")
    private String customerAddress;

    /**
     * 站线名称
     */
    @TableField(value = "station_line_name")
    private String stationLineName;

    /**
     * 联系方式
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮编
     */
    @TableField(value = "post_code")
    private String postCode;

    /**
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    @TableField(value = "user_type")
    private Integer userType;

    /**
     * 用户重要性：字典未定义
     */
    @TableField(value = "user_priority")
    private Integer userPriority;

    /**
     * 是否短时停电：0-否，1-是
     */
    @TableField(value = "bool_short_time")
    private Integer boolShortTime;

    /**
     * 所属台区
     */
    @TableField(value = "region")
    private String region;

    /**
     * 电系编号
     */
    @TableField(value = "electrical_number")
    private String electricalNumber;

    /**
     * 电压等级
     */
    @TableField(value = "voltage_level")
    private String voltageLevel;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 所属接入点
     */
    @TableField(value = "access_point")
    private String accessPoint;

    /**
     * 所属接入点名称
     */
    @TableField(value = "access_point_name")
    private String accessPointName;

    /**
     * 装接容量
     */
    @TableField(value = "capacity")
    private String capacity;

    /**
     * 是否重复停电：0-否，1-是
     */
    @TableField(value = "bool_repeat_power_cut")
    private Integer boolRepeatPowerCut;

    /**
     * 上次停电时间
     */
    @TableField(value = "last_power_cut_time")
    private LocalDateTime lastPowerCutTime;

    /**
     * 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
     */
    @TableField(value = "assign_status")
    private String assignStatus;

    /**
     * 派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @TableField(value = "assign_method")
    private Integer assignMethod;

    /**
     * 派发时间
     */
    @TableField(value = "assign_time")
    private LocalDateTime assignTime;

    /**
     * 派发负责方(工程队id)
     */
    @TableField(value = "engineers_team_id")
    private Serializable engineersTeamId;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @TableField(value = "feedback_status")
    private String feedbackStatus;

    /**
     * 反馈时间
     */
    @TableField(value = "feedback_time")
    private LocalDateTime feedbackTime;

    /**
     * 取消原因
     */
    @TableField(value = "cancel_reason")
    private String cancelReason;

    /**
     * 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
     */
    @TableField(value = "cancel_assign_status")
    private String cancelAssignStatus;

    /**
     * 取消通知派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @TableField(value = "cancel_assign_method")
    private Integer cancelAssignMethod;

    /**
     * 取消通知反馈状态：510-未签，520-同意
     */
    @TableField(value = "cancel_feedback_status")
    private String cancelFeedbackStatus;

    /**
     * 取消通知反馈时间
     */
    @TableField(value = "cancel_feedback_time")
    private LocalDateTime cancelFeedbackTime;

    /**
     * 新的联系方式
     */
    @TableField(value = "backup_phone")
    private String backupPhone;

    /**
     * 拒签理由
     */
    @TableField(value = "rejected_reason")
    private String rejectedReason;

    /**
     * 扩展属性1
     */
    @TableField(value = "attribute1")
    private String attribute1;

    /**
     * 扩展属性2
     */
    @TableField(value = "attribute2")
    private String attribute2;

    /**
     * 扩展属性3
     */
    @TableField(value = "attribute3")
    private String attribute3;

    /**
     * 停电任务对象
     */
    @TableField(exist = false)
    private Task task;

    /**
     * 停电任务对象停电时间
     */
    @TableField(exist = false)
    private LocalDateTime startTime;

    /**
     * 超期状态，默认正常，只有未及时进行反馈才会动态计算这个状态
     */
    @TableField(exist = false)
    private String overdueStatus = "正常";

    /**
     * 工程队名称
     */
    @TableField(exist = false)
    private String engineersTeamName;

    /**
     * 停电任务执行状态
     */
    @TableField(exist = false)
    private String taskExecuteStatus;

    /**
     * 停电间隔天数
     */
    @TableField(exist = false)
    private int intervalDays;

    /**
     * 距离停电时长
     */
    @TableField(exist = false)
    private Integer distanceDuration;

}
