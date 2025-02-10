package com.tecpie.platform.task_notice.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 停电任务通知公告表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务通知公告表分页检索请求参数")
@Getter
@Setter
public class TaskNoticePageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private TaskNoticeQueryCommand condition;

  public TaskNoticeQueryCommand getCondition() {
    return this.condition != null ? this.condition : new TaskNoticeQueryCommand();
  }

}