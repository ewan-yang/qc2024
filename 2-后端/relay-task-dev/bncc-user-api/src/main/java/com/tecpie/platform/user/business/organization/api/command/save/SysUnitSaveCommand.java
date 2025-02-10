package com.tecpie.platform.user.business.organization.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 组织结构表 保存请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "组织结构表保存请求参数")
@Getter
@Setter
public class SysUnitSaveCommand {

  /**
   * 父节点ID
   */
  @Schema(description = "父节点ID")
  private Serializable parentId;

  /**
   * 根节点到父级组织的全路径
   */
  @Schema(description = "根节点到父级组织的全路径")
  private String relationPath;

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
   * 公司、部门的区分，1表示公司，2表示部门
   */
  @Schema(description = "公司、部门的区分，1表示公司，2表示部门")
  private Integer type;

  /**
   * 排序值
   */
  @Schema(description = "排序值")
  private Integer sort;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}