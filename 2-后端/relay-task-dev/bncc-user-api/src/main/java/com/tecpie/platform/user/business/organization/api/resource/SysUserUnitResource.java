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
 * 用户组织关系表 响应结果
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户组织关系表响应结果")
@Getter
@Setter
public class SysUserUnitResource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "id[备注缺失]")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 用户id
   */
  @Schema(description = "用户id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable userId;

  /**
   * 部门id
   */
  @Schema(description = "部门id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable unitId;

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