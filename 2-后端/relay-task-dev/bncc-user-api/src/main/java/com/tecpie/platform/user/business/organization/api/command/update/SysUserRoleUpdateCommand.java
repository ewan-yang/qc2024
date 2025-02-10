package com.tecpie.platform.user.business.organization.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户角色关联表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户角色关联表更新请求参数")
@Getter
@Setter
public class SysUserRoleUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 用户ID
   */
  @Schema(description = "用户ID")
  private Serializable userId;

  /**
   * 角色ID
   */
  @Schema(description = "角色ID")
  private Serializable roleId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}