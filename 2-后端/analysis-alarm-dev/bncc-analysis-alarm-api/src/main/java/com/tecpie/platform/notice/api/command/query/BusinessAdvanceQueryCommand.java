package com.tecpie.platform.notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 业务数据预警 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "业务数据预警检索请求参数")
@Getter
@Setter
public class BusinessAdvanceQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    private Integer alarmType;

    /**
     * 告警标题
     */
    @Schema(description = "告警标题")
    private String alarmTitle;

    /**
     * 告警内容
     */
    @Schema(description = "告警内容")
    private String alarmContent;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String businessCode;

    /**
     * 告警时间 起始
     */
    @Schema(description = "告警时间 起始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate alarmTimeBegin;

    /**
     * 告警时间  截止
     */
    @Schema(description = "告警时间 截止")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDate alarmTimeEnd;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.alarmTimeBegin == null) {
            this.alarmTimeBegin = LocalDate.now().plusMonths(-1);
        }
        LocalDate currDateLocal = LocalDate.now();
        if (this.alarmTimeEnd != null) {
            currDateLocal = this.alarmTimeEnd;
        }
        this.alarmTimeEnd = currDateLocal.plusDays(1);
    }

}