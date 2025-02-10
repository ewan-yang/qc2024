package com.tecpie.platform.task_user.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电任务用户反馈表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户反馈表分页检索请求参数")
@Getter
@Setter
public class TaskUserFeedbackLogQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
    private String feedbackStatus;

    /**
     * 停电任务通知用户类型：1-停电通知派发，2-停电通知取消
     */
    @Schema(description = "停电任务通知用户类型：1-停电通知派发，2-停电通知取消")
    private Integer feedbackType;

    /**
     * 停电任务通知用户ID
     */
    @Schema(description = "停电任务通知用户ID")
    private Serializable taskUserId;

    /**
     * 派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @Schema(description = "派发方式：1-送达现场，2-微信通知，3-营销通知")
    private Integer deliveryMethod;

    /**
     * 新的联系方式
     */
    @Schema(description = "新的联系方式")
    private String backupPhone;

    /**
     * 拒签理由
     */
    @Schema(description = "拒签理由")
    private String rejectedReason;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

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

    @Schema(description = "通知单id集合")
    private List<Serializable> taskUserIdList;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.createDateEnd != null) {
            this.createDateEnd = this.createDateEnd.plusDays(1);
        }
    }

}