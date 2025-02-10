package com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token表 列表检索请求参数
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Schema(description = "用户Token表分页检索请求参数")
@Getter
@Setter
public class SysUserTokenQueryCommand implements Serializable {

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