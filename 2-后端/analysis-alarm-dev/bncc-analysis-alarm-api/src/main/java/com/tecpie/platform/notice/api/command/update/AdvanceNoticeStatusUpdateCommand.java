package com.tecpie.platform.notice.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 预告警状态变更 请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Schema(description = "预告警状态变更请求参数")
public class AdvanceNoticeStatusUpdateCommand {

    /**
     * 业务类型：1-停电任务模块
     */
    @Schema(description = "业务类型：1-停电任务模块", required = true)
    @NotNull(message = "businessType[业务类型]不能为空")
    private Integer businessType;

    /**
     * 业务IdList
     */
    @Schema(description = "业务IdList", required = true)
    @NotNull(message = "businessIdList[业务IdList]不能为空")
    private List<Serializable> businessIdList;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警", required = true)
    @NotNull(message = "alarmType[告警类型]不能为空")
    private Integer alarmType;

    /**
     * 状态：0-取消，1-正常
     */
    @Schema(description = "状态：0-取消，1-正常", required = true)
    @NotNull(message = "status[状态]不能为空")
    private Integer status;

}