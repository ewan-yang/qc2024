package com.tecpie.platform.up_record.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 数据修改记录表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Schema(description = "数据修改记录表保存请求参数")
@Getter
@Setter
public class UpdateRecordSaveCommand {

    /**
     * 业务类型：1-停电通知模块，2-后续新增业务再定义
     */
    @Schema(description = "业务类型：1-停电通知模块，2-后续新增业务再定义", required = true)
    @NotNull(message = "businessType[业务类型：1-停电通知模块，2-后续新增业务再定义]不能为空")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @Schema(description = "业务数据ID，每个业务类型对应不同的表ID", required = true)
    @NotNull(message = "businessId[业务数据ID，每个业务类型对应不同的表ID]不能为空")
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