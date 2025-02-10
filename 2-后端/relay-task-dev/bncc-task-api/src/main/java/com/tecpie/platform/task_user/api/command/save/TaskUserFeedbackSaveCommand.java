package com.tecpie.platform.task_user.api.command.save;

import com.tecpie.platform.file.api.command.update.CommonFileUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户反馈表保存请求参数")
@Getter
@Setter
public class TaskUserFeedbackSaveCommand {

  @Schema(description = "停电任务通知用户id集合（用于一键反馈：第一优先级）")
  private List<Serializable> taskUserIdList;

  @Schema(description = "停电通知单id(用于一键反馈：第二优先级)")
  private Serializable taskId;

  @Schema(description = "用户类型集合（用于一键反馈：第二优先级）")
  private List<Integer> userTypeList;

  @Schema(description = "所属台区（用于一键反馈：第二优先级）")
  private String region;

  @Schema(description = "客户名称（用于一键反馈：第二优先级）")
  private String customerName;

  @Schema(description = "客户地址（用于一键反馈：第二优先级）")
  private String customerAddress;

  /**
   * 停电任务通知用户ID
   */
  @Schema(description = "停电任务通知用户ID")
  private Serializable taskUserId;

  /**
   * 反馈状态：310-未签，320-同意，330-拒签
   */
  @Schema(description = "注意：前端这里需要使用不同的反馈状态字典。正常任务反馈状态：310-未签，320-同意，330-拒签，取消任务反馈状态：510-未签，520-同意", required = true)
  @NotBlank(message = "feedbackStatus[反馈状态]不能为空")
  private String feedbackStatus;

  /**
   * 派发方式：1-送达现场，2-微信通知，3-营销通知
   */
  @Schema(description = "派发方式：1-送达现场，2-微信通知，3-营销通知")
  @NotNull(message = "deliveryMethod[派发方式]不能为空")
  private Integer deliveryMethod;

  /**
   * 新的联系方式
   */
  @Schema(description = "新的联系方式")
  private String backupPhone;

  /**
   * 拒签理由
   */
  @Schema(description = "拒签理由")
  private String rejectedReason;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

    /**
     * 图片List
     */
    @Schema(description = "图片List")
    private List<CommonFileUpdateCommand> commonFileList;

}