package com.tecpie.platform.notice.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运方下达风险预警
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
public class CarrierReleaseAdvanceNotice {

    private static final long serialVersionUID = 1L;

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
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 停电类型：1-计划停电
     */
    private Integer type;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    private Integer reason;

    /**
     * 变电站名称
     */
    private String stationName;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 停电范围
     */
    private String ranges;

    /**
     * 停电位置
     */
    private String location;

    /**
     * 工作内容
     */
    private String jobContent;

    /**
     * 未派发数量
     */
    private Integer unassignedQty;

    /**
     * 已派发数量
     */
    private Integer assignedQty;

    /**
     * 已取消数量
     */
    private Integer cancelledQty;

    /**
     * 拒签数量
     */
    private Integer rejectedQty;

    /**
     * 同意数量
     */
    private Integer agreedQty;

    /**
     * 未签数量
     */
    private Integer unsignedQty;

    /**
     * 执行状态
     */
    private String executeStatus;

    /**
     * 任务超期时长
     */
    private Integer overdueDuration;

    /**
     * 版本
     */
    private Long version;

    /**
     * 告警类型
     */
    private Integer alarmType = 2;

    /**
     * 计算任务超期时长
     */
    public void calOverdueDuration(Integer days) {
        if (days == null) {
            return;
        }
        int daysNum = (int) (this.startTime.toLocalDate().toEpochDay() - this.confirmTime.toLocalDate().toEpochDay());
        this.setOverdueDuration(days - daysNum);
    }
}