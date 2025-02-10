package com.tecpie.platform.report.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.LocalDateConverter;
import com.tecpie.platform.notice.api.convert.PlanItemExecuteStatusConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 停电统计分析 数据响应结果
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "停电统计分析 数据响应结果")
@Getter
@Setter
public class PlanStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 停电计划项ID
     */
    @Schema(description = "停电计划项ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 工程编号
     */
    @Schema(description = "工程编号")
    @ExcelProperty(value = "工程编号")
    private String projectCode;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     */
    @Schema(description = "执行状态")
    @ExcelProperty(value = "状态", converter = PlanItemExecuteStatusConvert.class)
    private String executeStatus;

    /**
     * 工程名称
     */
    @Schema(description = "工程名称")
    @ExcelProperty(value = "工程名称")
    private String projectName;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    @ExcelProperty(value = "项目类型")
    private String projectType;

    /**
     * 变电站/线路名称
     */
    @Schema(description = "变电站/线路名称")
    @ExcelProperty(value = "变电站/线路名称")
    private String stationLineName;

    /**
     * 停役时间
     */
    @Schema(description = "停役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停役时间",converter = LocalDateConverter.class)
    private LocalDate startTime;

    /**
     * 复役时间
     */
    @Schema(description = "复役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "复役时间",converter = LocalDateConverter.class)
    private LocalDate endTime;

    /**
     * 施工班组
     */
    @Schema(description = "施工班组")
    @ExcelProperty(value = "施工班组")
    private String constructionTeam;

    /**
     * 版本
     */
    @Schema(description = "版本")
    @ExcelProperty(value = "版本")
    private Long version;

    /**
     * 关联通知单
     */
    @Schema(description = "关联通知单")
    @ExcelProperty(value = "关联通知单")
    private String taskCode;

}