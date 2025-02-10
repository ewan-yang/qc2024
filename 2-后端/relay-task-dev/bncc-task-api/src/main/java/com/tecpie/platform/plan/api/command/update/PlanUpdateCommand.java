package com.tecpie.platform.plan.api.command.update;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecpie.platform.plan_item.api.command.update.PlanItemUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划表更新请求参数")
@Getter
@Setter
public class PlanUpdateCommand {

    /**
     * 计划标题
     */
    @Schema(description = "计划标题")
    private String title;

    /**
     * 计划所属年月
     */
    @Schema(description = "计划所属年月")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate planMonth;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 计划项List
     */
    @Schema(description = "计划项List")
    private List<PlanItemUpdateCommand> planItemList;
}