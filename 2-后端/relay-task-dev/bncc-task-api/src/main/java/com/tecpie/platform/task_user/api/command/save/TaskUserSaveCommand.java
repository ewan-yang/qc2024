package com.tecpie.platform.task_user.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 停电任务用户表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户表保存请求参数")
@Getter
@Setter
public class TaskUserSaveCommand {

    /**
     * 户号
     */
    @Schema(description = "户号", required = true)
    @NotBlank(message = "accountNumber[户号]不能为空")
    private String accountNumber;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", required = true)
    @NotBlank(message = "customerName[客户名称]不能为空")
    private String customerName;

    /**
     * 客户地址
     */
    @Schema(description = "客户地址")
    private String customerAddress;

    @Schema(description = "站线名称")
    private String stationLineName;

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
     * 是否短时停电：0-否，1-是
     */
    @Schema(description = "是否短时停电：0-否，1-是", required = true)
    @NotNull(message = "boolShortTime[是否短时停电：0-否，1-是]不能为空")
    private Integer boolShortTime;

    /**
     * 用户重要性：字典未定义
     */
    @Schema(description = "用户重要性：字典未定义")
    private Integer userPriority;

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
    @Schema(description = "电压等级", required = true)
    @NotBlank(message = "voltageLevel[电压等级]不能为空")
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