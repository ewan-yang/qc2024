package com.tecpie.platform.up_record.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据修改记录表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Schema(description = "数据修改记录表分页检索请求参数")
@Getter
@Setter
public class UpdateRecordPageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private UpdateRecordQueryCommand condition;

  public UpdateRecordQueryCommand getCondition() {
    return this.condition != null ? this.condition : new UpdateRecordQueryCommand();
  }

}