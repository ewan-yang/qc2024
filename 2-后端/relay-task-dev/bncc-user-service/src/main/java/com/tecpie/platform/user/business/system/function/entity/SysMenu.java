package com.tecpie.platform.user.business.system.function.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *  实体类
 *
 * @author tecpie
 * @since 2022-11-08
 */
@Getter
@Setter
@TableName("sys_menu")
public class SysMenu extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 编码
   */
  @TableField(value = "code")
  private String code;

  /**
   * 名称
   */
  @TableField(value = "name")
  private String name;

  /**
   * 父节点主键
   */
  @TableField(value = "parent_id")
  private Serializable parentId;

  /**
   * 主页面路径
   */
  @TableField(value = "component_path")
  private String componentPath;

  /**
   * 权限
   */
  @TableField(value = "permission")
  private String permission;

  /**
   * 排序
   */
  @TableField(value = "sort")
  private Integer sort;

  /**
   * 类型，1-内部链接，2-外部链接
   */
  @TableField(value = "type")
  private Integer type;

  /**
   * 链接
   */
  @TableField(value = "url")
  private String url;

  /**
   * 样式
   */
  @TableField(value = "css")
  private String css;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SysMenu)) {
      return false;
    }
    SysMenu sysMenu = (SysMenu)obj;
    if (null == sysMenu.getId() || null == id) {
      return false;
    } else {
      return this.getId().equals(sysMenu.getId());
    }
  }

  @Override
  public int hashCode() {
    return (null == id) ? 0 : id.hashCode();
  }
}
