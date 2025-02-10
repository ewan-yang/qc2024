package com.tecpie.platform.user.business.system.sys_operation_log.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表 分页检索请求参数
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Schema(description = "操作日志表分页检索请求参数")
@Getter
@Setter
public class OperationLogPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private OperationLogQueryCommand condition;

  public OperationLogQueryCommand getCondition() {
    return this.condition != null ? this.condition : new OperationLogQueryCommand();
  }

}