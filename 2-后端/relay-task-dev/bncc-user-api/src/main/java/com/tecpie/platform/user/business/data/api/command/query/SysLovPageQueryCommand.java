package com.tecpie.platform.user.business.data.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV定义表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV定义表分页检索请求参数")
@Getter
@Setter
public class SysLovPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = -3416962792187789722L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysLovQueryCommand condition;

  public SysLovQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysLovQueryCommand();
  }

}