package com.tecpie.platform.user.business.organization.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织结构表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "组织结构表分页检索请求参数")
@Getter
@Setter
public class SysUnitPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = 5913125493096237030L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysUnitQueryCommand condition;

  public SysUnitQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysUnitQueryCommand();
  }

}