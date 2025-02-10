package com.tecpie.platform.user.business.organization.api.command.update;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecpie.platform.user.business.organization.api.command.common.SysOnlyIdCommand;
import com.tecpie.platform.user.business.organization.api.enums.LoginFlagEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息表 更新请求参数
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Schema(description = "用户信息表更新请求参数")
@Getter
@Setter
public class SysUserUpdateCommand {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Serializable id;

    /**
     * 登录名
     */
    @Schema(description = "登录名")
    private String code;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 工号
     */
    @Schema(description = "工号")
    private String workNumber;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 性别(男/女)
     */
    @Schema(description = "性别(男/女)")
    private String sex;

    /**
     * 职务
     */
    @Schema(description = "职务")
    private String position;

    /**
     * 派发负责方(工程队id)
     */
    @Schema(description = "派发负责方(工程队id)")
    private Serializable engineersTeamId;

    @Schema(description = "auditType[备注缺失]")
    private String auditType;

    /**
     * 头像图片链接地址
     */
    @Schema(description = "头像图片链接地址")
    private String headimgurl;

    /**
     * 办公电话
     */
    @Schema(description = "办公电话")
    private String telephone;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 密码修改日期
     */
    @Schema(description = "密码修改日期")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime passwordDate;

    /**
     * 生日
     */
    @Schema(description = "生日")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime birthDate;

    /**
     * 注册时间
     */
    @Schema(description = "注册时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime entryDate;

    /**
     * 登录成功时间
     */
    @Schema(description = "登录成功时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime workDate;

    /**
     * IP地址范围，分号分隔，支持*通配符
     */
    @Schema(description = "IP地址范围，分号分隔，支持*通配符")
    private String ipRange;

    /**
     * 最后登录失败IP
     */
    @Schema(description = "最后登录失败IP")
    private String loginFailIp;

    /**
     * 最后登录失败时间
     */
    @Schema(description = "最后登录失败时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime loginFailDate;

    /**
     * 是否可登录，0-长期锁定，1-可登录，2-短期锁定
     */
    @Schema(description = "是否可登录，0-长期锁定，1-可登录，2-短期锁定")
    private LoginFlagEnum loginFlag;

    /**
     * 登录成功时间
     */
    @Schema(description = "登录成功时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime loginSuccessDate;

    /**
     * 登录成功IP
     */
    @Schema(description = "登录成功IP")
    private String loginSuccessIp;

    /**
     * 锁定时间
     */
    @Schema(description = "锁定时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime lockDate;

    /**
     * token验证
     */
    @Schema(description = "token验证")
    private String token;

    /**
     * 登录失败次数
     */
    @Schema(description = "登录失败次数")
    private Integer loginFailCount;

    /**
     * 版本号
     */
    @Schema(description = "版本号")
    private Integer rev;

    /**
     * 姓
     */
    @Schema(description = "姓")
    private String first;

    /**
     * 名
     */
    @Schema(description = "名")
    private String last;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 用户所属角色列表
     */
    @Schema(description = "用户所属角色列表")
    private List<SysOnlyIdCommand> roleList;

    /**
     * 用户所属部门列表
     */
    @Schema(description = "用户所属部门列表")
    private List<SysOnlyIdCommand> unitList;

}