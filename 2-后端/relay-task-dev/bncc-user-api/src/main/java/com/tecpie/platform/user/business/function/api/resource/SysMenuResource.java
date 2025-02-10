package com.tecpie.platform.user.business.function.api.resource;

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
 * 响应结果
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Schema(description = "响应结果")
@Getter
@Setter
public class SysMenuResource implements
    TreeNode<SysMenuResource>, Serializable {

  private static final long serialVersionUID = 3486271306633077529L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

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
   * 父节点主键
   */
  @Schema(description = "父节点主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable parentId;

  @Schema(description = "父节点名称")
  private String parentName;

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
   * 子菜单
   */
  @Schema(description = "子菜单")
  private List<SysMenuResource> children;

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
  public void setTreeItemChildren(List<SysMenuResource> children) {
    this.setChildren(children);
  }
}