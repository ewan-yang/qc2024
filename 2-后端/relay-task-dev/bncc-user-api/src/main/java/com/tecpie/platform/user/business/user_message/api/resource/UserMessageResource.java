package com.tecpie.platform.user.business.user_message.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户消息表 响应结果
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Schema(description = "用户消息表响应结果")
@Getter
@Setter
public class UserMessageResource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "id[备注缺失]")
  @JsonSerialize(using = ToStringSerializer.class)
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
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable userId;

  /**
   * 消息跳转目标ID
   */
  @Schema(description = "消息跳转目标ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable entityId;

  /**
   * 消息内容
   */
  @Schema(description = "消息内容")
  private String remark;

  /**
   * 状态 0:已读 1:未读
   */
  @Schema(description = "状态 0:已读 1:未读")
  private Integer status;


  /**
   * 创建人ID
   */
  @Schema(description = "创建人ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDate;

  /**
   * 更新人ID
   */
  @Schema(description = "更新人ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 更新时间
   */
  @Schema(description = "更新时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

}