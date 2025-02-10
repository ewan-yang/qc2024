package com.tecpie.platform.user.business.function.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单列表检索请求参数
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Schema(description = "菜单分页检索请求参数")
@Getter
@Setter
public class SysMenuQueryCommand implements Serializable {

  private static final long serialVersionUID = 5200010776870369998L;
  @Schema(description = "编码")
  private String code;

  @Schema(description = "名称")
  private String name;

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