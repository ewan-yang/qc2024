package com.tecpie.platform.notice.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 预警信息 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "预警信息分页检索请求参数")
@Getter
@Setter
public class TaskAdvanceNoticePageQueryCommand extends PageCommand {

    /**
     * 检索条件
     */
    @Schema(description = "检索条件")
    private TaskAdvanceNoticeQueryCommand condition;

    public TaskAdvanceNoticeQueryCommand getCondition() {
        return this.condition != null ? this.condition : new TaskAdvanceNoticeQueryCommand();
    }

}