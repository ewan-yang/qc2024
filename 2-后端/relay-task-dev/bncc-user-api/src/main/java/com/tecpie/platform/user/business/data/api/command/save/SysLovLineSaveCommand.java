package com.tecpie.platform.user.business.data.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * LOV明细行 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV明细行保存请求参数")
@Getter
@Setter
public class SysLovLineSaveCommand {

  /**
   * LOV主键
   */
  @Schema(description = "LOV主键")
  private Serializable lovId;

  /**
   * 编码值
   */
  @Schema(description = "编码值", required = true)
  @NotBlank(message = "code[编码值]不能为空")
  private String code;

  /**
   * 显示值
   */
  @Schema(description = "显示值", required = true)
  @NotBlank(message = "name[显示值]不能为空")
  private String name;

  /**
   * 值
   */
  @Schema(description = "值")
  private String value;

  /**
   * 显示排序
   */
  @Schema(description = "显示排序")
  private Integer sort;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}