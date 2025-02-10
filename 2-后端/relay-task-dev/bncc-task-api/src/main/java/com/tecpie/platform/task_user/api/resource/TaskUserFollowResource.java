package com.tecpie.platform.task_user.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务用户跟进情况表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户跟进情况表响应结果")
@Getter
@Setter
public class TaskUserFollowResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键ID, 自增
   */
  @Schema(description = "主键ID, 自增")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 停电任务通知用户表ID
   */
  @Schema(description = "停电任务通知用户表ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable taskUserId;

  /**
   * 跟进方式：1-送达现场，2-微信通知，3-营销通知
   */
  @Schema(description = "跟进方式：1-送达现场，2-微信通知，3-营销通知")
  private Integer followMethod;

  /**
   * 对接人员
   */
  @Schema(description = "对接人员")
  private String dockUser;

  /**
   * 联系电话
   */
  @Schema(description = "联系电话")
  private String telPhone;

  /**
   * 反馈状态：310-未签，320-同意，330-拒签
   */
  @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
  private String feedbackStatus;

  /**
   * 跟进情况说明
   */
  @Schema(description = "跟进情况说明")
  private String followDesc;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  /**
   * 状态：0-停用，1-启用
   */
  @Schema(description = "状态：0-停用，1-启用")
  private Integer status;


  /**
   * 创建人
   */
  @Schema(description = "创建人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDate;

  /**
   * 修改人
   */
  @Schema(description = "修改人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 修改时间
   */
  @Schema(description = "修改时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

}