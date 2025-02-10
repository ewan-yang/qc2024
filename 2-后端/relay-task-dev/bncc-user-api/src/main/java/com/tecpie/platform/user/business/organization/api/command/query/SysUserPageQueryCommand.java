package com.tecpie.platform.user.business.organization.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户信息表分页检索请求参数")
@Getter
@Setter
public class SysUserPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = 689822063844749438L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysUserQueryCommand condition;

  public SysUserQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysUserQueryCommand();
  }

}