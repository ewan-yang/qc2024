package com.tecpie.platform.report.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电统计 数据统计实体类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023-08-03
 */
@Getter
@Setter
public class PowerCutStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属台区
     */
    private String region;

    /**
     * 停电任务通知表ID，与t_task表关联
     */
    private Serializable taskId;

    /**
     * 所属台区用户数量
     */
    private String regionUserCount;

    /**
     * 变电站名称
     */
    private String stationName;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    private Integer reason;

}
