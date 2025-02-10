package com.tecpie.platform.user.business.function.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统权限表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统权限表更新请求参数")
@Getter
@Setter
public class SysPermissionUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 资源ID
   */
  @Schema(description = "资源ID")
  private Serializable resourceId;

  /**
   * 操作ID
   */
  @Schema(description = "操作ID")
  private Serializable operationId;

  /**
   * 权限code
   */
  @Schema(description = "权限code")
  private String permissionCode;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}