package com.tecpie.platform.user.business.data.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV定义表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV定义表更新请求参数")
@Getter
@Setter
public class SysLovUpdateCommand {

  /**
   * LOV_ID
   */
  @Schema(description = "LOV_ID")
  private Serializable id;

  /**
   * LOV编码
   */
  @Schema(description = "LOV编码")
  private String code;

  /**
   * LOV名称
   */
  @Schema(description = "LOV名称")
  private String name;

  /**
   * 系统模块
   */
  @Schema(description = "系统模块")
  private String module;

  /**
   * 是否鉴权 - 0不鉴权,1:鉴权
   */
  @Schema(description = "是否鉴权 - 0不鉴权,1:鉴权")
  private Integer type;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  /**
   * LOV 明细行
   */
  @Schema(description = "LOV 明细行")
  private List<SysLovLineUpdateCommand> lovLineList = new ArrayList<>();

}