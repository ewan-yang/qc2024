package com.tecpie.platform.seq.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 序列表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Schema(description = "序列表分页检索请求参数")
@Getter
@Setter
public class SeqGenPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SeqGenQueryCommand condition;

  public SeqGenQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SeqGenQueryCommand();
  }

}