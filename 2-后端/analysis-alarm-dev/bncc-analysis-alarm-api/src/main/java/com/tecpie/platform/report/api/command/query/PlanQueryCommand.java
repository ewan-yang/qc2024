package com.tecpie.platform.report.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 停电计划统计 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "停电计划统计 检索请求参数")
@Getter
@Setter
@Slf4j
public class PlanQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    private String projectType;

    /**
     * 变电站/线路名称
     */
    @Schema(description = "变电站/线路名称")
    private String stationLineName;

    /**
     * 施工班组
     */
    @Schema(description = "施工班组")
    private String constructionTeam;

    /**
     * 停役时间 起始
     */
    @Schema(description = "停电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateBegin;

    /**
     * 停役时间  截止
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