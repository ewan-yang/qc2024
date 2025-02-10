package com.tecpie.platform.user.business.user_message.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户消息表 更新请求参数
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Schema(description = "用户消息表更新请求参数")
@Getter
@Setter
public class UserMessageUpdateCommand {

  @Schema(description = "id[备注缺失]")
  private Serializable id;

  /**
   * 消息类型
   */
  @Schema(description = "消息类型")
  private Integer type;

  /**
   * 接收消息用户ID
   */
  @Schema(description = "接收消息用户ID")
  private Serializable userId;

  /**
   * 消息跳转目标ID
   */
  @Schema(description = "消息跳转目标ID")
  private Serializable entityId;

  /**
   * 消息内容
   */
  @Schema(description = "消息内容")
  private String remark;

}