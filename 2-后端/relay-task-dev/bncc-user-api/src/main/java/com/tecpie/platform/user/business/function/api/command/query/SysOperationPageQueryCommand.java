package com.tecpie.platform.user.business.function.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作信息表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "操作信息表分页检索请求参数")
@Getter
@Setter
public class SysOperationPageQueryCommand extends PageCommand {

  private static final long serialVersionUID = -6935936076178481426L;
  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private SysOperationQueryCommand condition;

  public SysOperationQueryCommand getCondition() {
    return this.condition != null ? this.condition : new SysOperationQueryCommand();
  }

}