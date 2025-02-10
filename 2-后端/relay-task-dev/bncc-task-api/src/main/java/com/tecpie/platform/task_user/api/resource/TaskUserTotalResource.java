package com.tecpie.platform.task_user.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 停电通知用户 统计响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Getter
@Setter
@Schema(description = "停电通知用户 统计响应结果")
public class TaskUserTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 未派发数量
     */
    @Schema(description = "未派发数量")
    private Integer undistributedQty;

    /**
     * 未签数量
     */
    @Schema(description = "未签数量")
    private Integer noFeedbackQty;

    /**
     * 拒签数量
     */
    @Schema(description = "拒签数量")
    private Integer refusedSignQty;

    /**
     * 已超时数量
     */
    @Schema(description = "已超时数量")
    private Integer timeOutQty;

    /**
     * 取消通知未派发数量
     */
    @Schema(description = "取消通知未派发数量")
    private Integer cancelUndistributedQty;

    /**
     * 取消通知未签数量
     */
    @Schema(description = "取消通知未签数量")
    private Integer cancelNoFeedbackQty;

}