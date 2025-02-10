package com.tecpie.platform.notice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * 预告警表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_task_advance_notice")
public class TaskAdvanceNotice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电任务模块
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 业务ID
     */
    @TableField(value = "business_id")
    private Serializable businessId;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @TableField(value = "alarm_type")
    private Integer alarmType;

    /**
     * 告警标题
     */
    @TableField(value = "alarm_title")
    private String alarmTitle;

    /**
     * 告警内容
     */
    @TableField(value = "alarm_content")
    private String alarmContent;

    /**
     * 告警时间
     */
    @TableField(value = "alarm_time")
    private LocalDateTime alarmTime;

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

}
