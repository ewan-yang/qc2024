package com.tecpie.platform.task_notice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务通知公告表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task_notice")
public class TaskNotice extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 类型：1-通知，2-代办
   */
  @TableField(value = "type")
  private Integer type;

  /**
   * 标题
   */
  @TableField(value = "title")
  private String title;

  /**
   * 内容
   */
  @TableField(value = "content")
  private String content;

  /**
   * 预发布时间，只有当前时间大于等于这个时间的消息才会显示
   */
  @TableField(value = "push_time")
  private LocalDateTime pushTime;

  /**
   * 跳转的链接，这里一般是外链或前端路由地址
   */
  @TableField(value = "link_url")
  private String linkUrl;

    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;

  /**
   * 读取状态：0-未读，1-已读
   */
  @TableField(value = "read_status")
  private Integer readStatus;

  /**
   * 读取时间
   */
  @TableField(value = "read_time")
  private LocalDateTime readTime;

  /**
   * 读取用户ID
   */
  @TableField(value = "read_user_id")
  private Long readUserId;

}
