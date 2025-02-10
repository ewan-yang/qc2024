package com.tecpie.platform.user.business.user_message.api.command.query;

import com.tecpie.library.common.business.controller.command.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户消息表 分页检索请求参数
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Schema(description = "用户消息表分页检索请求参数")
@Getter
@Setter
public class UserMessagePageQueryCommand extends PageCommand {

  /**
   * 检索条件
   */
  @Schema(description = "检索条件")
  private UserMessageQueryCommand condition;

  public UserMessageQueryCommand getCondition() {
    return this.condition != null ? this.condition : new UserMessageQueryCommand();
  }

}