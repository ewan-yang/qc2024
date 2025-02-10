package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预告警表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "预告警表响应结果")
@Getter
@Setter
public class TaskAdvanceNoticeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 业务类型：1-停电任务模块
     */
    @Schema(description = "业务类型：1-停电任务模块")
    private Integer businessType;

    /**
     * 业务ID
     */
    @Schema(description = "业务ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable businessId;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    private Integer alarmType;

    /**
     * 告警标题
     */
    @Schema(description = "告警标题")
    private String alarmTitle;

    /**
     * 告警内容
     */
    @Schema(description = "告警内容")
    private String alarmContent;

    /**
     * 告警时间
     */
    @Schema(description = "告警时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime alarmTime;

    /**
     * 扩展属性1
     */
    @Schema(description = "扩展属性1")
    private String attribute1;

    /**
     * 扩展属性2
     */
    @Schema(description = "扩展属性2")
    private String attribute2;

    /**
     * 扩展属性3
     */
    @Schema(description = "扩展属性3")
    private String attribute3;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 状态：0-停用，1-启用
     */
    @Schema(description = "状态：0-停用，1-启用")
    private Integer status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime updateDate;

}