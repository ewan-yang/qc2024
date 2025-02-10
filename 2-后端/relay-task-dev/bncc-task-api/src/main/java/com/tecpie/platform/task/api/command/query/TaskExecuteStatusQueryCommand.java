package com.tecpie.platform.task.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 停电任务执行状态表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务执行状态表分页检索请求参数")
@Getter
@Setter
public class TaskExecuteStatusQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @Schema(description = "任务ID")
    private Serializable taskId;

    /**
     * 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
     */
    @Schema(description = "执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成")
    private String executeStatus;

}