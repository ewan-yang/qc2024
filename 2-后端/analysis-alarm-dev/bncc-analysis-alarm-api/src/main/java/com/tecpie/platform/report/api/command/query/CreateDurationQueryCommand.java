package com.tecpie.platform.report.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * 创建时长 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "创建时长 检索请求参数")
@Getter
@Setter
public class CreateDurationQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电范围
     */
    @Schema(description = "停电范围")
    private String ranges;

    /**
     * 停电原因
     */
    @Schema(description = "停电原因")
    private String reason;

    /**
     * 确认时间 起始
     */
    @Schema(description = "确认时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate confirmTimeBegin;

    /**
     * 确认时间  截止
     */
    @Schema(description = "确认时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate confirmTimeEnd;

    /**
     * 停电通知 状态集合
     */
    @Schema(description = "停电通知 状态集合")
    @JsonIgnore
    private List<String> executeStatusList;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        LocalDate currDateLocal = LocalDate.now();
        // 默认查询本月
        if (this.getConfirmTimeBegin() == null) {
            // 添加条件为本月第一天
            this.setConfirmTimeBegin(currDateLocal.with(TemporalAdjusters.firstDayOfMonth()));
        }
        // 获取当前日期
        if (this.getConfirmTimeEnd() == null) {
            this.setConfirmTimeEnd(currDateLocal.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

}