package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户组织关系表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_user_unit")
public class SysUserUnit extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 用户id
   */
  @TableField(value = "user_id")
  private Serializable userId;

  /**
   * 部门id
   */
  @TableField(value = "unit_id")
  private Serializable unitId;

}
