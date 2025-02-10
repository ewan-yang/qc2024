package com.tecpie.platform.plan.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 停电计划表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划表响应结果")
@Getter
@Setter
public class PlanResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 计划编号
     */
    @Schema(description = "计划编号")
    private String code;

    /**
     * 计划标题
     */
    @Schema(description = "计划标题")
    private String title;

    /**
     * 计划所属年月
     */
    @Schema(description = "计划所属年月")
    @JsonFormat(pattern = DatePattern.NORM_MONTH_PATTERN, timezone = "GMT+8")
    private LocalDate planMonth;

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
     * 总数
     */
    @Schema(description = "总数")
    private int total;

    /**
     * 未关联数量
     */
    @Schema(description = "未关联数量")
    private int unassociatedQty;

    /**
     * 已关联数量
     */
    @Schema(description = "已关联数量")
    private int associated;

}