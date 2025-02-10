package com.tecpie.platform.task_user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.file.entity.CommonFile;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task_user_feedback_log")
public class TaskUserFeedbackLog extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 停电任务通知用户ID
   */
  @TableField(value = "task_user_id")
  private Serializable taskUserId;

  /**
   * 停电任务通知用户类型：1-停电通知派发，2-停电通知取消
   */
  @TableField(value = "feedback_type")
  private Integer feedbackType;


    /**
     * 反馈状态：310-未签，320-同意，330-拒签
   */
  @TableField(value = "feedback_status")
  private String feedbackStatus;

  /**
   * 派发方式：1-送达现场，2-微信通知，3-营销通知
   */
  @TableField(value = "delivery_method")
  private Integer deliveryMethod;

  /**
   * 新的联系方式
   */
  @TableField(value = "backup_phone")
  private String backupPhone;

  /**
   * 拒签理由
   */
  @TableField(value = "rejected_reason")
  private String rejectedReason;

    /**
     * 文件List
     */
    @TableField(exist = false)
    private List<CommonFile> commonFileList;
}
