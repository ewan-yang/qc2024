package com.tecpie.platform.user.business.organization.api.command.save;

import com.tecpie.platform.user.business.organization.api.command.common.SysOnlyIdCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色信息表保存请求参数")
@Getter
@Setter
public class SysRoleSaveCommand {

  /**
   * 角色编码
   */
  @Schema(description = "角色编码", required = true)
  @NotBlank(message = "code[角色编码]不能为空")
  private String code;

  /**
   * 角色名称
   */
  @Schema(description = "角色名称", required = true)
  @NotBlank(message = "name[角色名称]不能为空")
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