package com.tecpie.platform.user.business.data.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV定义表 响应结果
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV定义表响应结果")
@Getter
@Setter
public class SysLovResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * LOV_ID
   */
  @Schema(description = "LOV_ID")
  @JsonSerialize(using = ToStringSerializer.class)
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
   * 状态 - 0:停用,1:启用
   */
  @Schema(description = "状态 - 0:停用,1:启用")
  private Integer status;


  /**
   * 创建人
   */
  @Schema(description = "创建人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDate;

  /**
   * 最后更新人
   */
  @Schema(description = "最后更新人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 最后更新时间
   */
  @Schema(description = "最后更新时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

  /**
   * LOV 明细行
   */
  @Schema(description = "Lov明细行")
  private List<SysLovLineResource> lovLineList = new ArrayList<>();

}