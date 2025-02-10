package com.tecpie.platform.user.business.organization.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色信息表分页检索请求参数")
@Getter
@Setter
public class SysRolePageQueryCommand extends PageCommand {

  private static final long serialVersionUID = 4202461434362187477L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysRoleQueryCommand condition;

  public SysRoleQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysRoleQueryCommand();
  }

}