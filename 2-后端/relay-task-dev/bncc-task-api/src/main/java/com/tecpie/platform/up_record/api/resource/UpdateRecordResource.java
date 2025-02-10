package com.tecpie.platform.up_record.api.resource;

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
 * 数据修改记录表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Schema(description = "数据修改记录表响应结果")
@Getter
@Setter
public class UpdateRecordResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 业务类型：1-停电通知模块，2-后续新增业务再定义
     */
    @Schema(description = "业务类型：1-停电通知模块，2-后续新增业务再定义")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @Schema(description = "业务数据ID，每个业务类型对应不同的表ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long businessId;

    /**
     * 更新前版本json数据
     */
    @Schema(description = "更新前版本json数据")
    private String preJson;

    /**
     * 更新后版本json数据
     */
    @Schema(description = "更新后版本json数据")
    private String afterJson;

    /**
     * 修改原因
     */
    @Schema(description = "修改原因")
    private String reason;

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