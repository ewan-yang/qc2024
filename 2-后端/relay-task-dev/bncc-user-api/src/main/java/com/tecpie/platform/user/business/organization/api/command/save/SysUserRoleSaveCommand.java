package com.tecpie.platform.user.business.organization.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户角色关联表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户角色关联表保存请求参数")
@Getter
@Setter
public class SysUserRoleSaveCommand {

  /**
   * 用户ID
   */
  @Schema(description = "用户ID", required = true)
  @NotNull(message = "userId[用户ID]不能为空")
  private Serializable userId;

  /**
   * 角色ID
   */
  @Schema(description = "角色ID", required = true)
  @NotNull(message = "roleId[角色ID]不能为空")
  private Serializable roleId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}