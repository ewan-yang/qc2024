package com.tecpie.platform.up_record.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据修改记录表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Schema(description = "数据修改记录表分页检索请求参数")
@Getter
@Setter
public class UpdateRecordQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电通知模块，2-后续新增业务再定义
     */
    @Schema(description = "业务类型：1-停电通知模块，2-后续新增业务再定义")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @Schema(description = "业务数据ID，每个业务类型对应不同的表ID")
    private Long businessId;

    /**
     * 创建开始时间
     */
    @Schema(description = "创建开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateBegin;

    /**
     * 创建结束时间
     */
    @Schema(description = "创建结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateEnd;

}