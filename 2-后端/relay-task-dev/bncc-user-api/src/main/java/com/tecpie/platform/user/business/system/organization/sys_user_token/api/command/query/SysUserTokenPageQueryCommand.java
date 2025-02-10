package com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token表 分页检索请求参数
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Schema(description = "用户Token表分页检索请求参数")
@Getter
@Setter
public class SysUserTokenPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysUserTokenQueryCommand condition;

  public SysUserTokenQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysUserTokenQueryCommand();
  }

}