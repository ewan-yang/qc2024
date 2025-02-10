package com.tecpie.platform.task_user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 停电任务用户跟进情况表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task_user_follow")
public class TaskUserFollow extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 停电任务通知用户表ID
   */
  @TableField(value = "task_user_id")
  private Serializable taskUserId;

  /**
   * 跟进方式：1-送达现场，2-微信通知，3-营销通知
   */
  @TableField(value = "follow_method")
  private Integer followMethod;

  /**
   * 对接人员
   */
  @TableField(value = "dock_user")
  private String dockUser;

  /**
   * 联系电话
   */
  @TableField(value = "tel_phone")
  private String telPhone;

  /**
   * 反馈状态：310-未签，320-同意，330-拒签
   */
  @TableField(value = "feedback_status")
  private String feedbackStatus;

  /**
   * 跟进情况说明
   */
  @TableField(value = "follow_desc")
  private String followDesc;

}
