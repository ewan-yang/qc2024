package com.tecpie.platform.task.api.command.query;

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
 * 停电任务统计 请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务统计请求参数")
@Getter
@Setter
public class TaskTotalQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 停电状态List 只供内部使用
     */
    @JsonIgnore
    private List<String> executeStatusList;

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