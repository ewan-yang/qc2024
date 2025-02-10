package com.tecpie.platform.engineers.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程队表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Schema(description = "工程队表分页检索请求参数")
@Getter
@Setter
public class EngineersTeamPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private EngineersTeamQueryCommand condition;

  public EngineersTeamQueryCommand getCondition() {
    return this.condition != null ? this.condition : new EngineersTeamQueryCommand();
  }

}