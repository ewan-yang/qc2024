package com.tecpie.platform.user.business.organization.api.resource;

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
 * 用户角色关联表 响应结果
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户角色关联表响应结果")
@Getter
@Setter
public class SysUserRoleResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 用户ID
   */
  @Schema(description = "用户ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable userId;

  /**
   * 角色ID
   */
  @Schema(description = "角色ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable roleId;

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