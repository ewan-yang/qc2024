package com.tecpie.platform.file.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件和图片表 分页检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Schema(description = "文件和图片表分页检索请求参数")
@Getter
@Setter
public class CommonFilePageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private CommonFileQueryCommand condition;

  public CommonFileQueryCommand getCondition() {
    return this.condition != null ? this.condition : new CommonFileQueryCommand();
  }

}