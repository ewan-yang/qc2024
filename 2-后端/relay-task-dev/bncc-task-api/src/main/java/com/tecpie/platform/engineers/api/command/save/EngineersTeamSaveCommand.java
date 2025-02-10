package com.tecpie.platform.engineers.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 工程队表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Schema(description = "工程队表保存请求参数")
@Getter
@Setter
public class EngineersTeamSaveCommand {

    /**
     * 工程队名称
     */
    @Schema(description = "工程队名称", required = true)
    @NotBlank(message = "name[工程队名称]不能为空")
    private String name;

    /**
     * 所属区域
     */
    @Schema(description = "所属区域")
    private String area;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}