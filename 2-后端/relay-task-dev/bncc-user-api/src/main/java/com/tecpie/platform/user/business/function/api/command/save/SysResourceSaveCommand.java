package com.tecpie.platform.user.business.function.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统资源表保存请求参数")
@Getter
@Setter
public class SysResourceSaveCommand {

  /**
   * 资源编码
   */
  @Schema(description = "资源编码", required = true)
  @NotBlank(message = "code[资源编码]不能为空")
  private String code;

  /**
   * 资源名称
   */
  @Schema(description = "资源名称", required = true)
  @NotBlank(message = "name[资源名称]不能为空")
  private String name;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  @Schema(description = "权限列表")
  private List<SysPermissionSaveCommand> permissionList;
}