package com.tecpie.platform.report.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 创建时长数据统计实体类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023-08-03
 */
@Getter
@Setter
public class TaskChangeStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电任务通知Id
     */
    private Serializable id;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 停电任务通知来源
     */
    private Long taskSource;

    /**
     * 停电任务通知来源名称
     */
    private String taskSourceName;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    private LocalDateTime endTime;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    private Integer reason;

    /**
     * 停电范围
     */
    private String ranges;

    /**
     * 停电户数
     */
    private Integer taskUserCount;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 执行状态
     */
    private String executeStatus;

    /**
     * 版本
     */
    private Long version;
}
