package com.tecpie.platform.task_user.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 停电任务用户表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户表更新请求参数")
@Getter
@Setter
public class TaskUserUpdateCommand {

  /**
   * 主键ID, 自增
   */
  @Schema(description = "主键ID, 自增")
  private Serializable id;

  /**
   * 户号
   */
  @Schema(description = "户号")
  private String accountNumber;

  /**
   * 客户名称
   */
  @Schema(description = "客户名称")
  private String customerName;

  /**
   * 客户地址
   */
  @Schema(description = "客户地址")
  private String customerAddress;

  /**
   * 联系方式
   */
  @Schema(description = "联系方式")
  private String phone;

  /**
   * 邮编
   */
  @Schema(description = "邮编")
  private String postCode;

  /**
   * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
   */
  @Schema(description = "用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核")
  private Integer userType;

  /**
   * 用户重要性：字典未定义
   */
  @Schema(description = "用户重要性：字典未定义")
  private Integer userPriority;

  /**
   * 是否短时停电：0-否，1-是
   */
  @Schema(description = "是否短时停电：0-否，1-是")
  private Integer boolShortTime;

  /**
   * 所属台区
   */
  @Schema(description = "所属台区")
  private String region;

  /**
   * 电系编号
   */
  @Schema(description = "电系编号")
  private String electricalNumber;

  /**
   * 电压等级
   */
  @Schema(description = "电压等级")
  private String voltageLevel;

  /**
   * 地址
   */
  @Schema(description = "地址")
  private String address;

  /**
   * 所属接入点
   */
  @Schema(description = "所属接入点")
  private String accessPoint;

  /**
   * 所属接入点名称
   */
  @Schema(description = "所属接入点名称")
  private String accessPointName;

  /**
   * 装接容量
   */
  @Schema(description = "装接容量")
  private String capacity;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}