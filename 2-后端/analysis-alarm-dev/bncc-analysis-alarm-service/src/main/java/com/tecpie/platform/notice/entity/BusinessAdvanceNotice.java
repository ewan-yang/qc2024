package com.tecpie.platform.notice.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 业务预告警 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
public class BusinessAdvanceNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电任务模块
     */
    private Integer businessType;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    private Integer alarmType;

    /**
     * 告警标题
     */
    private String alarmTitle;

    /**
     * 告警内容
     */
    private String alarmContent;

    /**
     * 告警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 业务ID
     */
    private Serializable businessId;

    /**
     * 业务编号
     */
    private String businessCode;

    /**
     * 回执单号
     */
    private String receiptCode;

    /**
     * 业务内容
     */
    private String businessContent;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    private LocalDateTime endTime;

}
