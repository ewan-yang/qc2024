package com.tecpie.platform.user.business.system.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * LOV定义表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_lov")
public class SysLov extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * LOV编码
   */
  @TableField(value = "code")
  private String code;

  /**
   * LOV名称
   */
  @TableField(value = "name")
  private String name;

  /**
   * 系统模块
   */
  @TableField(value = "module")
  private String module;

  /**
   * 是否鉴权 - 0不鉴权,1:鉴权
   */
  @TableField(value = "type")
  private Integer type;

  /**
   * LOV 明细行
   */
  @TableField(exist = false)
  private List<SysLovLine> lovLineList = new ArrayList<>();

}
