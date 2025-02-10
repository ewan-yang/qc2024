package com.tecpie.platform.rule.api.resource;

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
 * 告警规则维护 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "告警规则维护响应结果")
@Getter
@Setter
public class TaskAdvanceRuleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 规则编号
     */
    @Schema(description = "规则编号")
    private String ruleCode;

    /**
     * 规则名称
     */
    @Schema(description = "规则名称")
    private String ruleName;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)")
    private Integer days;

    /**
     * 区别参数1
     */
    @Schema(description = "区别参数1")
    private String params1;

    /**
     * 区别参数2
     */
    @Schema(description = "区别参数2")
    private String params2;

    /**
     * 区别参数3
     */
    @Schema(description = "区别参数3")
    private String params3;

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