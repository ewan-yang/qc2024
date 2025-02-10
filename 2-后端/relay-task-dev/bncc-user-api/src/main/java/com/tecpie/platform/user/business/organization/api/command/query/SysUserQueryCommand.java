package com.tecpie.platform.user.business.organization.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息表 列表检索请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户信息表分页检索请求参数")
@Getter
@Setter
public class SysUserQueryCommand implements Serializable {

  private static final long serialVersionUID = -1734349064444270828L;

  /**
   * 登录名
   */
  @Schema(description = "登录名")
  private String code;

  /**
   * 昵称
   */
  @Schema(description = "昵称")
  private String name;

  @Schema(description = "组织架构ID")
  private Serializable unitId;

  /**
   * 手机号码
   */
  @Schema(description = "手机号码")
  private String mobile;

  /**
   * 邮箱
   */
  @Schema(description = "邮箱")
  private String email;

  /**
   * 状态
   */
  @Schema(description = "状态")
  private Integer status;

  /**
   * ids
   */
  @Schema(description = "ids")
  private List<Long> ids;
}