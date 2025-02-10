package com.tecpie.platform.notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 公共查询字段 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "公共查询字段请求参数")
@Getter
@Setter
public class TaskAdvanceNoticeQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电任务模块，2-停电任务通知用户模块，3-停电计划模块
     */
    @Schema(description = "业务类型：1-停电任务模块，2-停电任务通知用户模块，3-停电计划模块")
    private Integer businessType;

    /**
     * 业务IdList
     */
    @Schema(description = "业务IdList")
    private List<Serializable> businessIdList;

    /**
     * 业务Id
     */
    @Schema(description = "业务Id")
    private Serializable businessId;

    /**
     * 告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警
     */
    @Schema(description = "告警类型：1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
    private Integer alarmType;

    /**
     * 告警类型List
     */
    @Schema(description = "告警类型List")
    private List<Integer> alarmTypeList;

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
     * 告警时间开始
     */
    @Schema(description = "告警时间开始")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime alarmTimeBegin;

    /**
     * 创建时间结束
     */
    @Schema(description = "创建时间结束")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime alarmTimeEnd;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.alarmTimeEnd != null) {
            this.alarmTimeEnd = this.alarmTimeEnd.plusDays(1);
        }
    }
}