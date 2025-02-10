package com.tecpie.platform.task.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务取消 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-11
 */
@Schema(description = "停电任务取消请求参数")
@Getter
@Setter
public class TaskCancelCommand {

    /**
     * 停电任务通知ID
     */
    @Schema(description = "停电任务通知ID", required = true)
    @NotNull(message = "taskId[停电任务通知ID]不能为空")
    private List<Serializable> taskIdList;

    /**
     * 是否派发取消通知：0-否，1-是
     */
    @Schema(description = "是否派发取消通知：0-否，1-是")
//    @NotNull(message = "boolAssignCancelNotice[是否派发取消通知：0-否，1-是]不能为空")
    private Integer boolAssignCancelNotice;

    /**
     * 取消理由
     */
    @Schema(description = "取消理由")
    private String cancelReason;

    /**
     * 取消前的停电任务状态
     */
    @Schema(description = "取消前的停电任务状态")
//    @NotNull(message = "taskId[取消前的停电计划状态]不能为空")
    private String executeStatus;
}