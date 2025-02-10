package com.tecpie.platform.plan_item.api.command.save;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 停电计划项表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划项表保存请求参数")
@Getter
@Setter
public class PlanItemSaveCommand {

    /**
     * 停电计划Id
     */
    @Schema(description = "停电计划Id")
    private Serializable planId;

    /**
     * 工程编号
     */
    @Schema(description = "工程编号", required = true)
    @NotBlank(message = "projectCode[工程编号]不能为空")
    private String projectCode;

    /**
     * 工程账号
     */
    @Schema(description = "工程账号", required = true)
    @NotBlank(message = "projectAccount[工程账号]不能为空")
    private String projectAccount;

    /**
     * 工程名称
     */
    @Schema(description = "工程名称", required = true)
    @NotBlank(message = "projectName[工程名称]不能为空")
    private String projectName;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型", required = true)
    @NotBlank(message = "projectType[项目类型]不能为空")
    private String projectType;

    /**
     * 停役时间
     */
    @Schema(description = "停役时间", required = true)
    @NotNull(message = "startTime[停役时间]不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startTime;

    /**
     * 复役时间
     */
    @Schema(description = "复役时间", required = true)
    @NotNull(message = "endTime[复役时间]不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate endTime;

    /**
     * 变电站/线路名称
     */
    @Schema(description = "变电站/线路名称", required = true)
    @NotBlank(message = "stationLineName[变电站/线路名称]不能为空")
    private String stationLineName;

    /**
     * 主要工作内容及相关验收部门		(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    @Schema(description = "主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)", required = true)
    @NotBlank(message = "jobContent[主要工作内容及相关验收部门(注:仅供参考,请以设计图纸及现场实际工作内容为准)]不能为空")
    private String jobContent;

    /**
     * 停役设备(注:仅供参考,请以现场勘查后实际停役单为准)
     */
    @Schema(description = "停役设备(注:仅供参考,请以现场勘查后实际停役单为准)")
    private String cosDevice;

    /**
     * 施工班组
     */
    @Schema(description = "施工班组")
    private String constructionTeam;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}