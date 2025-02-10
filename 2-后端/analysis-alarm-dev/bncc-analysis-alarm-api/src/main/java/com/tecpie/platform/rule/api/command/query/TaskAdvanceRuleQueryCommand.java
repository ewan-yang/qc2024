package com.tecpie.platform.rule.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 告警规则维护 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Schema(description = "告警规则维护分页检索请求参数")
public class TaskAdvanceRuleQueryCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 规则编号精确搜索
     */
    @Schema(description = "规则编号精确搜索")
    private String ruleCode;

    /**
     * 规则编号模糊搜索
     */
    @Schema(description = "规则编号模糊搜索")
    private String ruleCodeLike;

    /**
     * 规则名称
     */
    @Schema(description = "规则名称")
    private String ruleName;

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

}