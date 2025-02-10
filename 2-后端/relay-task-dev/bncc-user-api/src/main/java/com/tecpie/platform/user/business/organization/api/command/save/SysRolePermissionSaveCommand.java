package com.tecpie.platform.user.business.organization.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色与系统权限关联表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色与系统权限关联表保存请求参数")
@Getter
@Setter
public class SysRolePermissionSaveCommand {

  /**
   * 角色ID
   */
  @Schema(description = "角色ID", required = true)
  @NotNull(message = "roleId[角色ID]不能为空")
  private Serializable roleId;

  /**
   * 权限ID
   */
  @Schema(description = "权限ID", required = true)
  @NotNull(message = "permissionId[权限ID]不能为空")
  private Serializable permissionId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}