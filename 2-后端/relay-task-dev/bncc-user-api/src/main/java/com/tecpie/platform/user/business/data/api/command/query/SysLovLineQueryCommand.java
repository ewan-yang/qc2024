package com.tecpie.platform.user.business.data.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV明细行 列表检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "LOV明细行分页检索请求参数")
@Getter
@Setter
public class SysLovLineQueryCommand implements Serializable {

  private static final long serialVersionUID = -646668680640202483L;
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