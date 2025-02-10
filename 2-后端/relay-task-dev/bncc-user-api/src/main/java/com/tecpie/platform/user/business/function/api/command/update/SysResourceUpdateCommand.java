package com.tecpie.platform.user.business.function.api.command.update;

import com.tecpie.platform.user.business.function.api.command.save.SysPermissionSaveCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统资源表更新请求参数")
@Getter
@Setter
public class SysResourceUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 资源编码
   */
  @Schema(description = "资源编码")
  private String code;

  /**
   * 资源名称
   */
  @Schema(description = "资源名称")
  private String name;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  @Schema(description = "权限列表")
  private List<SysPermissionSaveCommand> permissionList;
}