package com.tecpie.platform.notice.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 计划变更预警 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
public class PlanChangeAdvanceNotice {
    private static final long serialVersionUID = 1L;

    /**
     * 计划项ID
     */
    private Serializable planItemId;

    /**
     * 工程编号
     */
    private String projectCode;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     */
    private String executeStatus;

    /**
     * 计划所属年月
     */
    private LocalDate planMonth;

    /**
     * 工程账号
     */
    private String projectAccount;

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
     * 停役设备(注:仅供参考,请以现场勘查后实际停役单为准)
     */
    private String cosDevice;

    /**
     * 停役时间
     */
    private LocalDate startTime;

    /**
     * 复役时间
     */
    private LocalDate endTime;

    /**
     * 主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    private String jobContent;

    /**
     * 施工班组
     */
    private String constructionTeam;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 告警类型
     */
    private Integer alarmType = 5;

    /**
     * 操作事件
     */
    private LocalDateTime updateDate;

}