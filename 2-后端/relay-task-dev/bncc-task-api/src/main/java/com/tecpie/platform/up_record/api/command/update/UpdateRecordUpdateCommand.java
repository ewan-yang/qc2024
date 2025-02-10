package com.tecpie.platform.up_record.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据修改记录表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Schema(description = "数据修改记录表更新请求参数")
@Getter
@Setter
public class UpdateRecordUpdateCommand {

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

}