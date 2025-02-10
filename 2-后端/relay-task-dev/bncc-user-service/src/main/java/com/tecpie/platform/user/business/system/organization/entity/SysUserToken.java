package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token表 实体类
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Getter
@Setter
@TableName("sys_user_token")
public class SysUserToken extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 租户ID
   */
  @TableField(value = "tenant_id")
  private Serializable tenantId;

  /**
   * 用户ID
   */
  @TableField(value = "user_id")
  private Serializable userId;

  /**
   * 客户端
   */
  @TableField(value = "client")
  private String client;

  /**
   * token验证
   */
  @TableField(value = "token")
  private String token;

  /**
   * refreshToken验证
   */
  @TableField(value = "refresh_token")
  private String refreshToken;

}
