package com.tecpie.platform.notice.entity;

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
@Getter
@Setter
public class ReleaseTimeOutAdvanceNotice {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Serializable taskUserId;

    /**
     * 回执编号
     */
    private String receiptCode;

    /**
     * 户号
     */
    private String accountNumber;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户地址
     */
    private String customerAddress;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户重要性
     */
    private Integer userPriority;

    /**
     * 是否短时停电：0-否，1-是
     */
    private Integer boolShortTime;

    /**
     * 所属台区
     */
    private String region;

    /**
     * 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
     */
    private String assignStatus;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    private String feedbackStatus;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
     */
    private String cancelAssignStatus;

    /**
     * 取消通知反馈状态：510-未签，520-同意
     */
    private String cancelFeedbackStatus;

    /**
     * 任务ID
     */
    private Serializable taskId;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    private LocalDateTime endTime;

    /**
     * 距离停电时长
     */
    private Integer distanceDuration;

    /**
     * 告警类型
     */
    private Integer alarmType = 1;

    public void catDistanceDuration() {
        this.setDistanceDuration((int) (this.startTime.toLocalDate().toEpochDay() - LocalDateTime.now().toLocalDate().toEpochDay()));
    }
}