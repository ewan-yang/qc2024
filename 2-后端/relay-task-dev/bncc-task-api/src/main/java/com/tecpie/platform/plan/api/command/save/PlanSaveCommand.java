package com.tecpie.platform.plan.api.command.save;

import cn.hutool.core.date.DatePattern;
import com.tecpie.platform.plan_item.api.command.save.PlanItemSaveCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Schema(description = "停电计划表保存请求参数")
@Getter
@Setter
public class PlanSaveCommand {

    /**
     * 计划标题
     */
    @Schema(description = "计划标题", required = true)
    @NotBlank(message = "title[计划标题]不能为空")
    private String title;

    /**
     * 计划所属年月
     */
    @Schema(description = "计划所属年月", required = true)
    @NotNull(message = "planMonth[计划所属年月]不能为空")
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
    private List<PlanItemSaveCommand> planItemList;

}