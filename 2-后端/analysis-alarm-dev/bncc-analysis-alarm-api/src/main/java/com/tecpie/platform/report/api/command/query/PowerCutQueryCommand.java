package com.tecpie.platform.report.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 停电统计 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "停电统计 检索请求参数")
@Getter
@Setter
public class PowerCutQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    private String region;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    private String taskCode;

    /**
     * 变电站名称
     */
    @Schema(description = "变电站名称")
    private String stationName;

    /**
     * 线路名称
     */
    @Schema(description = "线路名称")
    private String lineName;

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

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        LocalDate currDateLocal = LocalDate.now();
        // 默认查询本月
        if (this.getStartDateBegin() == null) {
            // 添加条件为本月第一天
            this.setStartDateBegin(currDateLocal.with(TemporalAdjusters.firstDayOfMonth()));
        }
        // 获取当前日期
        if (this.getStartDateEnd() == null) {
            this.setStartDateEnd(currDateLocal.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

}