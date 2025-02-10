package com.tecpie.platform.user.business.organization.api.command.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 只保留 ID，用于角色等保存时关联使用
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "仅ID关联入参")
@Getter
@Setter
public class SysOnlyIdCommand implements Serializable {

  private static final long serialVersionUID = 486439578027883269L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Serializable id;

}
