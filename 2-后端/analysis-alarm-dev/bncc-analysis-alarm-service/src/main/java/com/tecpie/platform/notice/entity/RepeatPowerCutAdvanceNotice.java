package com.tecpie.platform.notice.entity;

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
@Getter
@Setter
public class RepeatPowerCutAdvanceNotice {

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
     * 电系编号
     */
    private String electricalNumber;

    /**
     * 电压等级
     */
    private String voltageLevel;

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
     * 是否重复停电：0-否，1-是
     */
    private int boolRepeatPowerCut;

    /**
     * 上次停电时间
     */
    private LocalDateTime lastPowerCutTime;

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
     * 本次停电时间
     */
    private LocalDateTime thisPowerOutageTime;

    /**
     * 停电间隔天数
     */
    private Integer intervalDays;

    /**
     * 告警类型
     */
    private Integer alarmType = 4;

    public void catIntervalDays() {
        if (this.thisPowerOutageTime == null) {
            this.setThisPowerOutageTime(this.getStartTime());
        }
        if (this.lastPowerCutTime == null) {
            return;
        }
        this.setIntervalDays((int) (this.thisPowerOutageTime.toLocalDate().toEpochDay() - this.lastPowerCutTime.toLocalDate().toEpochDay()));
    }

}