package com.tecpie.platform.user.business.system.organization.sys_user_token.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token表 响应结果
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Schema(description = "用户Token表响应结果")
@Getter
@Setter
public class SysUserTokenResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 租户ID
   */
  @Schema(description = "租户ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable tenantId;

  /**
   * 用户ID
   */
  @Schema(description = "用户ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable userId;

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

}