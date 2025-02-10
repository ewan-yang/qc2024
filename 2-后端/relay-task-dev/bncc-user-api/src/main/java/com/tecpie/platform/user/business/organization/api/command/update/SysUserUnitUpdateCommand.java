package com.tecpie.platform.user.business.organization.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户组织关系表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户组织关系表更新请求参数")
@Getter
@Setter
public class SysUserUnitUpdateCommand {

  @Schema(description = "id[备注缺失]")
  private Serializable id;

  /**
   * 用户id
   */
  @Schema(description = "用户id")
  private Serializable userId;

  /**
   * 部门id
   */
  @Schema(description = "部门id")
  private Serializable unitId;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}