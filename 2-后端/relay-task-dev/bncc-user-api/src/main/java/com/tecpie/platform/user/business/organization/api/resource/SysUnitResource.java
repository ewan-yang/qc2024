package com.tecpie.platform.user.business.organization.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.library.common.util.tree.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织结构表 响应结果
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "组织结构表响应结果")
@Getter
@Setter
public class SysUnitResource implements TreeNode<SysUnitResource>, Serializable {

  private static final long serialVersionUID = 1375652620822954083L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 父节点ID
   */
  @Schema(description = "父节点ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable parentId;

  /**
   * 父节点名称
   */
  @Schema(description = "父节点名称")
  private String parentName;

  /**
   * 根节点到父级组织的全路径
   */
  @Schema(description = "根节点到父级组织的全路径")
  private String relationPath;

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

  /**
   * 状态 - 0:停用,1:启用
   */
  @Schema(description = "状态 - 0:停用,1:启用")
  private Integer status;


  /**
   * 创建人
   */
  @Schema(description = "创建人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDate;

  /**
   * 最后更新人
   */
  @Schema(description = "最后更新人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 最后更新时间
   */
  @Schema(description = "最后更新时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

  /**
   * 子部门
   */
  @Schema(description = "子部门")
  private List<SysUnitResource> children;

  @Override
  @JsonIgnore(false)
  public String getTreeItemId() {
    return id.toString();
  }

  @Override
  @JsonIgnore(false)
  public String getTreeItemPid() {
    return Optional.ofNullable(parentId).map(Object::toString).orElse("0");
  }

  @Override
  public void setTreeItemChildren(List<SysUnitResource> children) {
    this.setChildren(children);
  }
}