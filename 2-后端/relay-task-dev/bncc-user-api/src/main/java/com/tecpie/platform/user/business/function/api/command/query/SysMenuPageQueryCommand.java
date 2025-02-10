package com.tecpie.platform.user.business.function.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *  分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Schema(description = "分页检索请求参数")
@Getter
@Setter
public class SysMenuPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = -5979981778027662997L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysMenuQueryCommand condition;

  public SysMenuQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysMenuQueryCommand();
  }

}