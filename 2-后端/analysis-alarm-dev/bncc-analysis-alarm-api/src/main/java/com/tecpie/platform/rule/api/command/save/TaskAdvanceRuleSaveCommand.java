package com.tecpie.platform.rule.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 告警规则维护 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "告警规则维护保存请求参数")
@Getter
@Setter
public class TaskAdvanceRuleSaveCommand {

    /**
     * 规则名称
     */
    @Schema(description = "规则名称", required = true)
    @NotBlank(message = "ruleName[规则名称]不能为空")
    private String ruleName;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)", required = true)
    @NotNull(message = "days[时长(天)]不能为空")
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

}