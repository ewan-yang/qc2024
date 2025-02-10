package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色与系统权限关联表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_role_permission")
public class SysRolePermission extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 角色ID
   */
  @TableField(value = "role_id")
  private Serializable roleId;

  /**
   * 权限ID
   */
  @TableField(value = "permission_id")
  private Serializable permissionId;

}
