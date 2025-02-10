package com.tecpie.platform.user.business.organization.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户组织关系表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户组织关系表保存请求参数")
@Getter
@Setter
public class SysUserUnitSaveCommand {

  /**
   * 用户id
   */
  @Schema(description = "用户id", required = true)
  @NotNull(message = "userId[用户id]不能为空")
  private Serializable userId;

  /**
   * 部门id
   */
  @Schema(description = "部门id", required = true)
  @NotNull(message = "unitId[部门id]不能为空")
  private Serializable unitId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}