package com.tecpie.platform.user.business.data.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV明细行 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV明细行分页检索请求参数")
@Getter
@Setter
public class SysLovLinePageQueryCommand extends PageCommand {

  private static final long serialVersionUID = -5453747181765898499L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysLovLineQueryCommand condition;

  public SysLovLineQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysLovLineQueryCommand();
  }

}