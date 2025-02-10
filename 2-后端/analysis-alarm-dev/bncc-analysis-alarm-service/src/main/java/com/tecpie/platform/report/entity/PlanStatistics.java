package com.tecpie.platform.report.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 停电计划统计 数据统计实体类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023-08-03
 */
@Getter
@Setter
public class PlanStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电计划项ID
     */
    private Serializable id;

    /**
     * 工程编号
     */
    private String projectCode;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     */
    private String executeStatus;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 变电站/线路名称
     */
    private String stationLineName;

    /**
     * 停役时间
     */
    private LocalDate startTime;

    /**
     * 复役时间
     */
    private LocalDate endTime;

    /**
     * 施工班组
     */
    private String constructionTeam;

    /**
     * 版本
     */
    private Long version;

    /**
     * 关联通知单
     */
    private String taskCode;

}
