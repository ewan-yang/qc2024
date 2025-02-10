package com.tecpie.platform.notice.api.resource;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.notice.api.convert.AlarmTypeConvert;
import com.tecpie.platform.notice.api.convert.LocalDateConverter;
import com.tecpie.platform.notice.api.convert.PlanItemExecuteStatusConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 计划变更预警 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "计划变更预警响应结果")
@Getter
@Setter
public class PlanChangeAdvanceNoticeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划项ID
     */
    @Schema(description = "计划项ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable planItemId;

    /**
     * 工程编号
     */
    @Schema(description = "工程编号")
    @ExcelProperty(value = "工程编号")
    private String projectCode;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     */
    @Schema(description = "执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行")
    @ExcelProperty(value = "状态", converter = PlanItemExecuteStatusConvert.class)
    private String executeStatus;

    /**
     * 计划所属年月
     */
    @Schema(description = "计划所属年月")
    @JsonFormat(pattern = "yyyy年MM月", timezone = "GMT+8")
    @ExcelProperty(value = "计划年月", converter = LocalDateConverter.class)
    private LocalDate planMonth;

    /**
     * 工程账号
     */
    @Schema(description = "工程账号")
    @ExcelProperty(value = "工程账号")
    private String projectAccount;

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
     * 停役设备(注:仅供参考,请以现场勘查后实际停役单为准)
     */
    @Schema(description = "停役设备(注:仅供参考,请以现场勘查后实际停役单为准)")
    @ExcelProperty(value = "停役设备(注:仅供参考,请以现场勘查后实际停役单为准)")
    private String cosDevice;

    /**
     * 停役时间
     */
    @Schema(description = "停役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "停役时间", converter = LocalDateConverter.class)
    private LocalDate startTime;

    /**
     * 复役时间
     */
    @Schema(description = "复役时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ExcelProperty(value = "复役时间", converter = LocalDateConverter.class)
    private LocalDate endTime;

    /**
     * 主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    @Schema(description = "主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)")
    @ExcelProperty(value = "主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)")
    private String jobContent;

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
    private Integer version;

    /**
     * 告警类型
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    @ExcelProperty(value = "计划变更预警", converter = AlarmTypeConvert.class)
    private Integer alarmType;

    @Schema(description = "操作时间")
    @ExcelProperty(value = "操作时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ColumnWidth(20)
    private LocalDateTime updateDate;

}