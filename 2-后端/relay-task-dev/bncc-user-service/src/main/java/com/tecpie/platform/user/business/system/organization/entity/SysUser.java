package com.tecpie.platform.user.business.system.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.user.business.organization.api.enums.LoginFlagEnum;
import com.tecpie.platform.user.business.system.function.entity.SysPermission;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息表 实体类
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("sys_user")
public class SysUser extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 登录名
   */
  @TableField(value = "code")
  private String code;

  /**
   * 昵称
   */
  @TableField(value = "name")
  private String name;

  /**
   * 工号
   */
  @TableField(value = "work_number")
  private String workNumber;

  /**
   * 密码
   */
  @TableField(value = "password")
  private String password;

  /**
   * 性别(男/女)
   */
  @TableField(value = "sex")
  private String sex;

  /**
   * 职务
   */
  @TableField(value = "position")
  private String position;

  /**
   * 派发负责方(工程队id)
   */
  @TableField(value = "engineers_team_id", insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
  private Serializable engineersTeamId;

  /**
   * TODO 了解具体作用
   */
  @TableField(value = "audit_type")
  private String auditType;

  /**
   * 头像图片链接地址
   */
  @TableField(value = "headimgurl")
  private String headimgurl;

  /**
   * 办公电话
   */
  @TableField(value = "telephone")
  private String telephone;

  /**
   * 手机号码
   */
  @TableField(value = "mobile")
  private String mobile;

  /**
   * 邮箱
   */
  @TableField(value = "email")
  private String email;

  /**
   * 密码修改日期
   */
  @TableField(value = "password_date")
  private LocalDateTime passwordDate;

  /**
   * 生日
   */
  @TableField(value = "birth_date")
  private LocalDateTime birthDate;

  /**
   * 注册时间
   */
  @TableField(value = "entry_date")
  private LocalDateTime entryDate;

  /**
   * 登录成功时间
   */
  @TableField(value = "work_date")
  private LocalDateTime workDate;

  /**
   * IP地址范围，分号分隔，支持*通配符
   */
  @TableField(value = "ip_range")
  private String ipRange;

  /**
   * 最后登录失败IP
   */
  @TableField(value = "login_fail_ip")
  private String loginFailIp;

  /**
   * 最后登录失败时间
   */
  @TableField(value = "login_fail_date")
  private LocalDateTime loginFailDate;

  /**
   * 是否可登录，0-长期锁定，1-可登录，2-短期锁定
   */
  @TableField(value = "login_flag")
  private LoginFlagEnum loginFlag;

  /**
   * 登录成功时间
   */
  @TableField(value = "login_success_date")
  private LocalDateTime loginSuccessDate;

  /**
   * 登录成功IP
   */
  @TableField(value = "login_success_ip")
  private String loginSuccessIp;

  /**
   * 锁定时间
   */
  @TableField(value = "lock_date")
  private LocalDateTime lockDate;

  /**
   * token验证
   */
  @TableField(value = "token")
  private String token;

  /**
   * 登录失败次数
   */
  @TableField(value = "login_fail_count")
  private Integer loginFailCount;

  /**
   * TODO 确认是否需要
   * 版本号
   */
  @TableField(value = "rev")
  private Integer rev;

  /**
   * 姓
   */
  @TableField(value = "first")
  private String first;

  /**
   * 名
   */
  @TableField(value = "last")
  private String last;

  /**
   * 用户所属角色列表
   */
  @TableField(exist = false)
  private List<SysRole> roleList;

  /**
   * 用户包括的权限列表 MyBatis 中的 Collection 会根据 Id 进行去重，因此如果一个用户属于多个角色，角色包含相同的 SysPermission，在
   * sysPermissionList 中不会有 Id 重复的 SysPermission
   */
  @TableField(exist = false)
  private List<SysPermission> permissionList;

  /**
   * 用户所属组织列表
   */
  @TableField(exist = false)
  private List<SysUnit> unitList;
}
