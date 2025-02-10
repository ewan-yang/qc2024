package com.tecpie.platform.task_user.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 停电任务用户跟进 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户跟进保存请求参数")
@Getter
@Setter
public class TaskUserFollowSaveCommand {

  /**
   * 停电任务通知用户表ID
   */
  @Schema(description = "停电任务通知用户表ID")
  private Serializable taskUserId;

  /**
   * 跟进方式：1-送达现场，2-微信通知，3-营销通知
   */
  @Schema(description = "跟进方式：1-送达现场，2-微信通知，3-营销通知", required = true)
  @NotNull(message = "followMethod[跟进方式]不能为空")
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

}