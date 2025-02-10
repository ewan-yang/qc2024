package com.tecpie.platform.user.business.system.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * LOV明细行 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_lov_line")
public class SysLovLine extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * LOV主键
   */
  @TableField(value = "lov_id")
  private Serializable lovId;

  /**
   * 编码值
   */
  @TableField(value = "code")
  private String code;

  /**
   * 显示值
   */
  @TableField(value = "name")
  private String name;

  /**
   * 值
   */
  @TableField(value = "value")
  private String value;

  /**
   * 显示排序
   */
  @TableField(value = "sort")
  private Integer sort;

}
