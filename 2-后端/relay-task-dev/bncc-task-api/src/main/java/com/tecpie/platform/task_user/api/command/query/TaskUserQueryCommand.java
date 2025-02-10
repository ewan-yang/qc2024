package com.tecpie.platform.task_user.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电任务用户表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户表分页检索请求参数")
@Getter
@Setter
@Accessors(chain = true)
public class TaskUserQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    private String receiptCode;

    /**
     * 停电任务通知表ID
     */
    @Schema(description = "停电任务通知表ID")
    private Serializable taskId;

    /**
     * 户号
     */
    @Schema(description = "户号")
    private String accountNumber;

    /**
     * 户号in查询
     */
    @Schema(description = "户号in查询")
    private List<String> accountNumberList;

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
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    @Schema(description = "用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核")
    private Integer  userType;

    /**
     * 用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核
     */
    @Schema(description = "用户类型：1-高大、2-低大、3-低非、4-居民、5-居委、6-物业、7-抄送用户、8-考核")
    private List<Integer>  userTypeList;

    /**
     * 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
     */
    @Schema(description = "派发状态：210-未派发，220-已派发，230-已反馈，240-不派发")
    private List<String> assignStatusList;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态")
    private String feedbackStatus;

    /**
     * 派发负责方(工程队id)
     */
    @Schema(description = "派发负责方(工程队id)，如果是一键派发，当前字段不作为查询条件，而是派发的工程队")
    private Serializable engineersTeamId;

    /**
     * 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
     */
    @Schema(description = "取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发")
    private List<String> cancelAssignStatusList;

    /**
     * 任务执行状态List
     */
    @Schema(description = "任务执行状态List, 可取值：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成")
    private List<String> taskExecuteStatusList;

    /**
     * 取消原因
     */
    @Schema(description = "取消原因，如果是一键取消派发，当前字段不作为查询条件")
    private String cancelReason;

    /**
     * 用户IdList
     */
    @Schema(description = "用户id集合")
    private List<Serializable> taskUserIdList;

    /**
     * 停电时间 起始
     */
    @Schema(description = "停电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateBegin;

    /**
     * 停电时间  截止
     */
    @Schema(description = "停电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateEnd;

    @Schema(description = "需要导出的字段（用户导出功能）")
    private List<String> fieldNameList;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startDateEnd != null) {
            this.startDateEnd = this.startDateEnd.plusDays(1);
        }
    }


}