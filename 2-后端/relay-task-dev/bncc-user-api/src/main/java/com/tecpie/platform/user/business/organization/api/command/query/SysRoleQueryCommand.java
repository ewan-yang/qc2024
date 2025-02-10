package com.tecpie.platform.user.business.organization.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表 列表检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "角色信息表分页检索请求参数")
@Getter
@Setter
public class SysRoleQueryCommand implements Serializable {

  private static final long serialVersionUID = 1234821306350027797L;
  /**
   * 编码
   */
  @Schema(description = "编码")
  private String code;

  /**
   * 名称
   */
  @Schema(description = "名称")
  private String name;

  /**
   * 状态
   */
  @Schema(description = "状态")
  private Integer status;

}