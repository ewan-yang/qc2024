package com.tecpie.platform.user.business.organization.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色与系统权限关联表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色与系统权限关联表更新请求参数")
@Getter
@Setter
public class SysRolePermissionUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 角色ID
   */
  @Schema(description = "角色ID")
  private Serializable roleId;

  /**
   * 权限ID
   */
  @Schema(description = "权限ID")
  private Serializable permissionId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}