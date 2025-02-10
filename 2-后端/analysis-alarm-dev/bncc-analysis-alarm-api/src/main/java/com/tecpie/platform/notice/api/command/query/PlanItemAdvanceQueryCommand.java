package com.tecpie.platform.notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 计划变更预警 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "计划变更预警列表检索请求参数")
@Getter
@Setter
public class PlanItemAdvanceQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    private String projectType;

    /**
     * 施工班组
     */
    @Schema(description = "施工班组")
    private String constructionTeam;

    /**
     * 变电站/线路名称
     */
    @Schema(description = "变电站/线路名称")
    private String stationLineName;

    /**
     * 主要工作内容及相关验收部门		(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    @Schema(description = "主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)")
    private String jobContent;

    /**
     * 计划年月
     */
    @Schema(description = "计划年月")
    private String planMonth;

    /**
     * 停役时间 - 起始
     */
    @Schema(description = "停役时间 - 起始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate startDateBegin;

    /**
     * 停役时间 - 截止
     */
    @Schema(description = "停役时间 - 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate startDateEnd;

    /**
     * 复役时间 - 起始
     */
    @Schema(description = "复役时间 - 起始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate endDateBegin;

    /**
     * 复役时间 - 截止
     */
    @Schema(description = "复役时间 - 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate endDateEnd;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startDateEnd != null) {
            this.startDateEnd = this.startDateEnd.plusDays(1);
        }
        if (this.endDateEnd != null) {
            this.endDateEnd = this.endDateEnd.plusDays(1);
        }
    }

}