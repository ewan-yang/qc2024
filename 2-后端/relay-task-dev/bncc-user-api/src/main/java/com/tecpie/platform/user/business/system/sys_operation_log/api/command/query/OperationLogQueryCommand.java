package com.tecpie.platform.user.business.system.sys_operation_log.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表 列表检索请求参数
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Schema(description = "操作日志表分页检索请求参数")
@Getter
@Setter
public class OperationLogQueryCommand implements Serializable{

  private static final long serialVersionUID = 1L;

  /**
  * 状态
  */
  @Schema(description = "状态")
  private Integer status;

  /**
  * 创建人
  */
  @Schema(description = "创建人")
  private Serializable createBy;

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
  private Serializable updateBy;

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