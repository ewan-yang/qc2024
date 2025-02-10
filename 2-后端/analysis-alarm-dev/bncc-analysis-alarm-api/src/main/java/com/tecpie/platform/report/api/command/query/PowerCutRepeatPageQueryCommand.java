package com.tecpie.platform.report.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "重复停电 分页检索请求参数")
@Getter
@Setter
public class PowerCutRepeatPageQueryCommand extends PageCommand {

    /**
     * 检索条件
     */
    @Schema(description = "检索条件")
    private PowerCutRepeatQueryCommand condition;

    public PowerCutRepeatQueryCommand getCondition() {
        return this.condition != null ? this.condition : new PowerCutRepeatQueryCommand();
    }

}