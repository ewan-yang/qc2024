package com.tecpie.platform.task.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 停电任务统计响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务统计响应结果")
@Getter
@Setter
public class TaskTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待提交数量
     */
    @Schema(description = "待提交数量")
    private Integer submitQty;

    /**
     * 待派发数量
     */
    @Schema(description = "待派发数量")
    private Integer releaseQty;

    /**
     * 执行中数量
     */
    @Schema(description = "执行中数量")
    private Integer executionQty;

    /**
     * 已取消数量
     */
    @Schema(description = "已取消数量")
    private Integer cancelQty;

    /**
     * 已反馈数量
     */
    @Schema(description = "已反馈数量")
    private Integer feedbackQty;

    /**
     * 已完成数量
     */
    @Schema(description = "已完成数量")
    private Integer finishedQty;

}