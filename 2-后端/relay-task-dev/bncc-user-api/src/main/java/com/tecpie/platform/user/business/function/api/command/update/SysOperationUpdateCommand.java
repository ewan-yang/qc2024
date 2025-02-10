package com.tecpie.platform.user.business.function.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 操作信息表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "操作信息表更新请求参数")
@Getter
@Setter
public class SysOperationUpdateCommand {

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

  /**
   * 操作编码
   */
  @Schema(description = "操作编码")
  private String code;

  /**
   * 操作名称
   */
  @Schema(description = "操作名称")
  private String name;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}