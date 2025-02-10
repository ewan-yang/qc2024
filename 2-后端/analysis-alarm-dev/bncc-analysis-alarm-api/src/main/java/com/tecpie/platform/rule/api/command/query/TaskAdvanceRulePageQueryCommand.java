package com.tecpie.platform.rule.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警规则维护表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "告警规则维护分页检索请求参数")
@Getter
@Setter
public class TaskAdvanceRulePageQueryCommand extends PageCommand {

    /**
     * 检索条件
     */
    @Schema(description = "检索条件")
    private TaskAdvanceRuleQueryCommand condition;

    public TaskAdvanceRuleQueryCommand getCondition() {
        return this.condition != null ? this.condition : new TaskAdvanceRuleQueryCommand();
    }

}