package com.tecpie.platform.task.api.resource;

import com.tecpie.platform.task_user.api.resource.TaskUserSumTotalResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 停电任务汇总 统计响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务汇总 统计响应结果")
@Getter
@Setter
public class TaskSumTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电通知总数量
     */
    @Schema(description = "停电通知总数量")
    private Integer taskSumQty;

    /**
     * 停电通知总数量同比
     */
    @Schema(description = "停电通知总数量同比")
    private Integer taskSumTb;

    /**
     * 停电通知总数量同比百分比
     */
    @Schema(description = "停电通知总数量同比百分比")
    private String taskSumTbPercentage;

    /**
     * 已完成通知数量
     */
    @Schema(description = "已完成通知数量")
    private Integer taskFinishedQty;

    /**
     * 已完成通知数量同比
     */
    @Schema(description = "已完成通知数量同比")
    private Integer taskFinishedTb;

    /**
     * 已完成通知数量同比百分比
     */
    @Schema(description = "已完成通知数量同比百分比")
    private String taskFinishedTbPercentage;

    /**
     * 已取消通知数量
     */
    @Schema(description = "已取消通知数量")
    private Integer taskCancelQty;

    /**
     * 已取消通知数量同比
     */
    @Schema(description = "已取消通知数量同比")
    private Integer taskCancelTb;

    /**
     * 已取消通知数量同比百分比
     */
    @Schema(description = "已取消通知数量同比百分比")
    private String taskCancelTbPercentage;

    /**
     * 停电用户相关统计对象
     */
    @Schema(description = "停电用户相关统计对象")
    private TaskUserSumTotalResource taskUserSumTotalResource;

}