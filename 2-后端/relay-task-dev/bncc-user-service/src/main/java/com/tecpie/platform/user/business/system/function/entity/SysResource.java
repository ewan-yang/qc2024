package com.tecpie.platform.user.business.system.function.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源表 实体类
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Getter
@Setter
@TableName("sys_resource")
public class SysResource extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 资源编码
   */
  @TableField(value = "code")
  private String code;

  /**
   * 资源名称
   */
  @TableField(value = "name")
  private String name;

  /**
   * 权限列表
   */
  @TableField(exist = false)
  private List<SysPermission> permissionList;
}
