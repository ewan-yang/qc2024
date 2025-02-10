package com.tecpie.platform.plan_item.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.plan.api.resource.PlanResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 停电计划项表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划项表响应结果")
@Getter
@Setter
public class PlanItemResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 停电计划ID
     */
    @Schema(description = "停电计划ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable planId;

    /**
     * 工程编号
     */
    @Schema(description = "工程编号")
    private String projectCode;

    /**
     * 工程账号
     */
    @Schema(description = "工程账号")
    private String projectAccount;

    /**
     * 工程名称
     */
    @Schema(description = "工程名称")
    private String projectName;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    private String projectType;

    /**
     * 停役时间
     */
    @Schema(description = "停役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate startTime;

    /**
     * 复役时间
     */
    @Schema(description = "复役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate endTime;

    /**
     * 变电站/线路名称
     */
    @Schema(description = "变电站/线路名称")
    private String stationLineName;

    /**
     * 主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    @Schema(description = "主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)")
    private String jobContent;

    /**
     * 停役设备(注:仅供参考,请以现场勘查后实际停役单为准)
     */
    @Schema(description = "停役设备(注:仅供参考,请以现场勘查后实际停役单为准)")
    private String cosDevice;

    /**
     * 施工班组
     */
    @Schema(description = "施工班组")
    private String constructionTeam;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     */
    @Schema(description = "执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行")
    private String executeStatus;

    /**
     * 版本
     */
    @Schema(description = "版本")
    private Long version;

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

    /**
     * 停电计划对象
     */
    @Schema(description = "停电计划对象")
    private PlanResource plan;

}