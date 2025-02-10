package com.tecpie.platform.plan.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 停电计划表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划表分页检索请求参数")
@Getter
@Setter
public class PlanPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private PlanQueryCommand condition;

  public PlanQueryCommand getCondition() {
    return this.condition != null ? this.condition : new PlanQueryCommand();
  }

}