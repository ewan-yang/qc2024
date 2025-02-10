package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 组织结构表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_unit")
public class SysUnit extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 父节点ID
   */
  @TableField(value = "parent_id")
  private Serializable parentId;

  /**
   * 根节点到父级组织的全路径
   */
  @TableField(value = "relation_path")
  private String relationPath;

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
   * 公司、部门的区分，1表示公司，2表示部门
   */
  @TableField(value = "type")
  private Integer type;

  /**
   * 排序值
   */
  @TableField(value = "sort")
  private Integer sort;

    /**
     * 用户Id
     */
    @TableField(exist = false)
    private Serializable userId;

}
