package com.tecpie.platform.user.business.system.function.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;

import com.tecpie.library.common.business.entity.condition.ConditionType;
import com.tecpie.library.common.business.entity.condition.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作信息表 实体类
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Getter
@Setter
@TableName("sys_operation")

public class SysOperation extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 操作编码
   */
  @TableField(value = "code")
  @PageCondition(orderBy = true, conditions = {ConditionType.LIKE})
  private String code;

  /**
   * 操作名称
   */
  @TableField(value = "name")
  private String name;

}
