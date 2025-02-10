package com.tecpie.platform.notice.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 预告警数量统计 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Schema(description = "预告警数量统计")
public class AdvanceNoticeTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划变更数量
     */
    @Schema(description = "计划变更数量")
    private int planItemChangeCount;

    /**
     * 下达风险数量
     */
    @Schema(description = "下达风险数量")
    private int carrierReleaseCount;

    /**
     * 下达超时数量
     */
    @Schema(description = "下达超时数量")
    private int releaseTimeOutCount;

    /**
     * 用户拒签数量
     */
    @Schema(description = "用户拒签数量")
    private int userRejectCount;

    /**
     * 重复停电数量
     */
    @Schema(description = "重复停电数量")
    private int repeatPowerCutCount;

}
