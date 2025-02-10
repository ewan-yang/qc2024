package com.tecpie.platform.notice.api.command.save;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 告警参数 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警参数 保存请求参数")
public class AdvanceParamCommand {

    /**
     * 业务ID
     */
    @Schema(description = "业务ID")
    private Serializable businessId;

    /**
     * 业务编号
     */
    @Schema(description = "业务编号")
    private String businessCode;

    /**
     * 通知单编号
     */
    @Schema(description = "通知单编号")
    private String taskCode;

    /**
     * 户号
     */
    @Schema(description = "户号")
    private String accountNumber;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 最近一次时间
     */
    @Schema(description = "最近一次时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime lastTime;

    /**
     * 告警时长(天)
     */
    @Schema(description = "告警时长(天)")
    private Integer days;


}
