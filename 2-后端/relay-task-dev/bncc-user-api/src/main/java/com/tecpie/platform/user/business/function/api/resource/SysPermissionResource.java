package com.tecpie.platform.user.business.function.api.resource;

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
 * 系统权限表 响应结果
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统权限表响应结果")
@Getter
@Setter
public class SysPermissionResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  @Schema(description = "操作ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable operationId;

  @Schema(description = "资源ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable resourceId;

  /**
   * 资源
   */
  @Schema(description = "资源")
  private SysResourceResource sysResource;

  /**
   * 操作
   */
  @Schema(description = "操作")
  private SysOperationResource sysOperation;

  /**
   * 权限code
   */
  @Schema(description = "权限code")
  private String permissionCode;

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