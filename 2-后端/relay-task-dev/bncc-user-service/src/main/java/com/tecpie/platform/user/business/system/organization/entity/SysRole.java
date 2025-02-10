package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_role")
public class SysRole extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 角色编码
   */
  @TableField(value = "code")
  private String code;

  /**
   * 角色名称
   */
  @TableField(value = "name")
  private String name;

  /**
   * 用户id
   */
  @TableField(exist = false)
  private Serializable userId;

  /**
   * 用户列表
   */
  @TableField(exist = false)
  private List<SysUser> userList;

  /**
   * 权限列表
   */
  @TableField(exist = false)
  private List<SysPermission> permissionList;
}
