package com.tecpie.platform.rule.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警规则维护 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "告警规则维护更新请求参数")
@Getter
@Setter
public class TaskAdvanceRuleUpdateCommand {

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

}