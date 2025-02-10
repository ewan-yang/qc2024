package com.tecpie.platform.task.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@Schema(description = "移动平台-工程队-首页-数据汇总响应结果")
public class EngineersSumResource {

    @Schema(description = "停电通知派发")
    private int assignQty;

    @Schema(description = "停电通知派发-同比增加")
    private int assignIncrease;

    @Schema(description = "停电通知派发-同比上涨百分比")
    private BigDecimal assignRisePct;

    @Schema(description = "取消停电派发")
    private int cancelAssignQty;

    @Schema(description = "取消停电派发-同比增加")
    private int cancelAssignIncrease;

    @Schema(description = "取消停电派发-同比上涨")
    private BigDecimal cancelAssignRisePct;

    @Schema(description = "派发超时")
    private int overTimeQty;

    @Schema(description = "派发超时-同比增加")
    private int overTimeIncrease;

    @Schema(description = "派发超时-同比上涨")
    private BigDecimal overTimeRisePct;

}
