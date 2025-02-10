package com.tecpie.platform.report.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接收时长 数据统计实体类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023-08-03
 */
@Getter
@Setter
public class ReceiveDurationStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电通知用户id
     */
    private Serializable id;

    /**
     * 停电任务通知表ID，与t_task表关联
     */
    private Serializable taskId;

    /**
     * 回执编号
     */
    private String receiptCode;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

    /**
     * 户号
     */
    private String accountNumber;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    private Integer userType;

    /**
     * 所属台区
     */
    private String region;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 派发时间
     */
    private LocalDateTime assignTime;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 时长(天)
     */
    private Integer hourDays;

    /**
     * 下达超时警告阈值（天）
     */
    private Integer alertDays;

    public Integer getHourDays() {
        if (this.getFeedbackTime() == null || this.getStartTime() == null) {
            return null;
        }
        return (int) (this.startTime.toLocalDate().toEpochDay() - this.getFeedbackTime().toLocalDate().toEpochDay());
    }
}
