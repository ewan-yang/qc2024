package com.tecpie.platform.user.business.function.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作信息表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "操作信息表保存请求参数")
@Getter
@Setter
public class SysOperationSaveCommand {

  /**
   * 操作编码
   */
  @Schema(description = "操作编码", required = true)
  @NotBlank(message = "code[操作编码]不能为空")
  private String code;

  /**
   * 操作名称
   */
  @Schema(description = "操作名称", required = true)
  @NotBlank(message = "name[操作名称]不能为空")
  private String name;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}