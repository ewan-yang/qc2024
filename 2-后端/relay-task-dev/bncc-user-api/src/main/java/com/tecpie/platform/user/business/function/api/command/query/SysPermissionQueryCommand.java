package com.tecpie.platform.user.business.function.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统权限表 列表检索请求参数
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Schema(description = "系统权限表分页检索请求参数")
@Getter
@Setter
public class SysPermissionQueryCommand implements Serializable {

  private static final long serialVersionUID = -7648422455568685105L;
  /**
   * 资源名称
   */
  @Schema(description = "资源名称")
  private String resourceName;

  /**
   * 操作名称
   */
  @Schema(description = "操作名称")
  private String operationName;

  /**
  * 状态
  */
  @Schema(description = "状态")
  private Integer status;

  /**
  * 创建人
  */
  @Schema(description = "创建人")
  private String createBy;

  /**
  * 创建开始时间
  */
  @Schema(description = "创建开始时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDateBegin;

  /**
  * 创建结束时间
  */
  @Schema(description = "创建结束时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDateEnd;

  /**
  * 更新人
  */
  @Schema(description = "更新人")
  private String updateBy;

  /**
  * 更新开始时间
  */
  @Schema(description = "更新开始时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDateBegin;

  /**
  * 更新结束时间
  */
  @Schema(description = "更新结束时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDateEnd;

}