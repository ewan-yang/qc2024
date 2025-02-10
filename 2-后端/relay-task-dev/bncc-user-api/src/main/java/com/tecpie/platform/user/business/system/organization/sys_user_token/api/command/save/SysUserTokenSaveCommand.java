package com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token表 保存请求参数
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Schema(description = "用户Token表保存请求参数")
@Getter
@Setter
public class SysUserTokenSaveCommand {

  /**
   * 租户ID
   */
  @Schema(description = "租户ID")
  private Long tenantId;

  /**
   * 用户ID
   */
  @Schema(description = "用户ID", required = true)
  @NotNull(message = "userId[用户ID]不能为空")
  private Long userId;

  /**
   * 客户端
   */
  @Schema(description = "客户端")
  private String client;

  /**
   * token验证
   */
  @Schema(description = "token验证")
  private String token;

  /**
   * refreshToken验证
   */
  @Schema(description = "refreshToken验证")
  private String refreshToken;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}