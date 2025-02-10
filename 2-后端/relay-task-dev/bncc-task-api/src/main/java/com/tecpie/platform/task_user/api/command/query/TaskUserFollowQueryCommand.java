package com.tecpie.platform.task_user.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 停电任务用户跟进情况表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户跟进情况表分页检索请求参数")
@Getter
@Setter
public class TaskUserFollowQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电任务通知用户ID
     */
    @Schema(description = "停电任务通知用户ID")
    private Serializable taskUserId;

    /**
     * 跟进方式：1-送达现场，2-微信通知，3-营销通知
     */
    @Schema(description = "跟进方式：1-送达现场，2-微信通知，3-营销通知")
    private Integer followMethod;

    /**
     * 对接人员
     */
    @Schema(description = "对接人员")
    private String dockUser;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String telPhone;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
    private String feedbackStatus;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private Serializable createBy;

    /**
     * 创建开始时间
     */
    @Schema(description = "创建开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateBegin;

    /**
     * 创建结束时间
     */
    @Schema(description = "创建结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateEnd;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.createDateEnd != null) {
            this.createDateEnd = this.createDateEnd.plusDays(1);
        }
    }

}