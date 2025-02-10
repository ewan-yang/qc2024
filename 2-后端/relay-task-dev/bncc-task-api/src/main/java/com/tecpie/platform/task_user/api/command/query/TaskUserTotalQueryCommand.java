package com.tecpie.platform.task_user.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电任务用户统计 请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Schema(description = "停电任务用户统计请求参数")
@Getter
@Setter
public class TaskUserTotalQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 派发状态
     */
    @JsonIgnore
    private String assignStatus;

    /**
     * 反馈状态
     */
    @JsonIgnore
    private String feedbackStatus;

    /**
     * 取消通知派发状态
     */
    @JsonIgnore
    private String cancelAssignStatus;

    /**
     * 取消通知反馈状态
     */
    @JsonIgnore
    private String cancelFeedbackStatus;

    /**
     * 派发负责方(工程队id)
     */
    @JsonIgnore
    private Serializable engineersTeamId;

    /**
     * 是否重复停电
     */
    @JsonIgnore
    private Integer boolRepeatPowerCut;

    /**
     * 停电状态List
     */
    @JsonIgnore
    private List<String> executeStatusList;

    /**
     * Sql类型
     */
    @JsonIgnore
    private String sqlType;

    /**
     * 创建时间 起始
     */
    @Schema(description = "创建时间 起始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateBegin;

    /**
     * 创建时间  截止
     */
    @Schema(description = "创建时间 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate createDateEnd;

    public LocalDate getCreateDateBegin() {
        if (this.createDateBegin == null) {
            this.createDateBegin = LocalDate.now().plusYears(-1);
        }
        return this.createDateBegin;
    }

    public LocalDate getCreateDateEnd() {
        if (this.createDateEnd == null) {
            this.createDateEnd = LocalDate.now();
        }
        this.createDateEnd = this.createDateEnd.plusDays(1);
        return this.createDateEnd;
    }
}