package com.tecpie.platform.plan.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * 首页-停电计划统计-响应结果
 */
@Schema(description = "首页-停电计划统计-响应结果")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    @Schema(description = "总数")
    private int total;

    /**
     * 待执行数
     */
    @Schema(description = "待执行数")
    private int beExecuteQty;

    /**
     * 执行中数
     */
    @Schema(description = "执行中数")
    private int executeQty;

    /**
     * 已完成数
     */
    @Schema(description = "Schema")
    private int completeQty;

    /**
     * 已取消数
     */
    @Schema(description = "已取消数")
    private int cancelQty;

    /**
     * 未执行数
     */
    @Schema(description = "未执行数")
    private int notExecuteQty;

}
