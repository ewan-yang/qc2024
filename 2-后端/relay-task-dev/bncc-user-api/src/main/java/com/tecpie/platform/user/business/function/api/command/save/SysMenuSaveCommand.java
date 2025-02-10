package com.tecpie.platform.user.business.function.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *  保存请求参数
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Schema(description = "保存请求参数")
@Getter
@Setter
public class SysMenuSaveCommand {

  /**
   * 编码
   */
  @Schema(description = "编码", required = true)
  @NotBlank(message = "code[编码]不能为空")
  private String code;

  /**
   * 名称
   */
  @Schema(description = "名称", required = true)
  @NotBlank(message = "name[名称]不能为空")
  private String name;

  /**
   * 父节点主键
   */
  @Schema(description = "父节点主键")
  private Serializable parentId;

  /**
   * 主页面路径
   */
  @Schema(description = "主页面路径")
  private String componentPath;

  /**
   * 权限
   */
  @Schema(description = "权限")
  private String permission;

  /**
   * 排序
   */
  @Schema(description = "排序")
  private Integer sort;

  /**
   * 类型，1-内部链接，2-外部链接
   */
  @Schema(description = "类型，1-内部链接，2-外部链接")
  private Integer type;

  /**
   * 链接
   */
  @Schema(description = "链接")
  private String url;

  /**
   * 样式
   */
  @Schema(description = "样式")
  private String css;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}