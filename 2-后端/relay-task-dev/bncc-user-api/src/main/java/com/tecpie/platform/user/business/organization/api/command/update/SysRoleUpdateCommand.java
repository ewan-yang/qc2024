package com.tecpie.platform.user.business.organization.api.command.update;

import com.tecpie.platform.user.business.organization.api.command.common.SysOnlyIdCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色信息表更新请求参数")
@Getter
@Setter
public class SysRoleUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 角色编码
   */
  @Schema(description = "角色编码")
  private String code;

  /**
   * 角色名称
   */
  @Schema(description = "角色名称")
  private String name;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  /**
   * 用户列表
   */
  @Schema(description = "用户列表")
  private List<SysOnlyIdCommand> userList;

  /**
   * 权限列表
   */
  @Schema(description = "权限列表")
  private List<SysOnlyIdCommand> permissionList;

}