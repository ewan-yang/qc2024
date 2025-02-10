package com.tecpie.platform.task_user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 停电任务用户备用联系方式表 实体类
 *
 * @author zhijie.tan
 * @since 2023-08-08
 */
@Getter
@Setter
@TableName("t_task_user_backup_phone")
public class TaskUserBackupPhone extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 户号
   */
  @TableField(value = "account_number")
  private String accountNumber;

  /**
   * 备用联系方式
   */
  @TableField(value = "backup_phone")
  private String backupPhone;

}
