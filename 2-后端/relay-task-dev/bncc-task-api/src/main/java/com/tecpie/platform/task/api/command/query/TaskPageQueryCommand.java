package com.tecpie.platform.task.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 停电任务表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务表分页检索请求参数")
@Getter
@Setter
public class TaskPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private TaskQueryCommand condition;

  public TaskQueryCommand getCondition() {
    return this.condition != null ? this.condition : new TaskQueryCommand();
  }

}