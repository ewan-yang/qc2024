package com.tecpie.platform.plan_item.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划项表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划项表分页检索请求参数")
@Getter
@Setter
public class PlanItemQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电计划Id
     */
    @Schema(description = "停电计划Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Serializable planId;

    /**
     * 工程名称
     */
    @Schema(description = "工程名称")
    private String projectName;

    /**
     * 工程编号
     */
    @Schema(description = "工程编号")
    private String projectCode;

    /**
     * 工程编号List
     */
    @Schema(description = "工程编号List")
    private List<String> projectCodeList;

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
    private LocalDate startTimeStart;

    /**
     * 停役时间 - 截止
     */
    @Schema(description = "停役时间 - 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate startTimeEnd;

    /**
     * 复役时间 - 起始
     */
    @Schema(description = "复役时间 - 起始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate endTimeStart;

    /**
     * 复役时间 - 截止
     */
    @Schema(description = "复役时间 - 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate endTimeEnd;

    @Schema(description = "施工班组")
    private String constructionTeam;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     * change : 2023/9/12   执行装袋修改为 010-未关联 020-已关联  其余状态删除
     */
    @Schema(description = "执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行")
    private String executeStatus;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startTimeEnd != null) {
            this.startTimeEnd = this.startTimeEnd.plusDays(1);
        }
        if (this.endTimeEnd != null) {
            this.endTimeEnd = this.endTimeEnd.plusDays(1);
        }
    }
}