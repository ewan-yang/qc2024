package com.tecpie.platform.user.business.system.function.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统权限表 实体类
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Getter
@Setter
@TableName("sys_permission")
public class SysPermission extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 权限code
   */
  @TableField(value = "permission_code")
  private String permissionCode;

  /**
   * 资源ID
   */
  private Serializable resourceId;

  /**
   * 操作ID
   */
  private Serializable operationId;

  /**
   * 资源
   */
  @TableField(exist = false)
  private SysResource sysResource;

  /**
   * 操作
   */
  @TableField(exist = false)
  private SysOperation sysOperation;

}
