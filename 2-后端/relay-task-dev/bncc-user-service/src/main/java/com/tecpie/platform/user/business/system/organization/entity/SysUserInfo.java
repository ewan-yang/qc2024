package com.tecpie.platform.user.business.system.organization.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息类PO
 *
 * @author liqi
 * @date 2020/6/3 11:28
 */
@Setter
@Getter
public class SysUserInfo {

  /**
   * 姓名
   */
  private String name;

  /**
   * 移动电话
   */
  private String phone;

  /**
   * 邮箱
   */
  private String email;

  /**
   * 头像图片URL
   */
  private String avatar;

  /**
   * 性别
   */
  private String gender;

  /**
   * 角色集合
   */
  private List<String> roles = new ArrayList<>();

  /**
   * 权限集合
   */
  private List<String> authorities = new ArrayList<>();

}