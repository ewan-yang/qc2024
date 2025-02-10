package com.tecpie.platform.user.business.function.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统权限表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统权限表分页检索请求参数")
@Getter
@Setter
public class SysPermissionPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = 6927996538963073302L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysPermissionQueryCommand condition;

  public SysPermissionQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysPermissionQueryCommand();
  }

}