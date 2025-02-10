package com.tecpie.platform.notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 下达分险预警 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Schema(description = "下达分险预警检索请求参数")
@Getter
@Setter
public class TaskAdvanceQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电时间 起始
     */
    @Schema(description = "停电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateBegin;

    /**
     * 停电时间  截止
     */
    @Schema(description = "停电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateEnd;

    /**
     * 送电时间 起始
     */
    @Schema(description = "送电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate endDateBegin;

    /**
     * 送电时间 截止
     */
    @Schema(description = "送电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate endDateEnd;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    private String taskCode;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 停电范围
     */
    @Schema(description = "停电范围")
    private String ranges;

    /**
     * 工作内容
     */
    @Schema(description = "工作内容")
    private String jobContent;

    /**
     * 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
     */
    @JsonIgnore
    private List<String> executeStatusList;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startDateBegin == null) {
            this.startDateBegin = LocalDate.now();
        }
        if (this.startDateEnd != null) {
            this.startDateEnd = this.startDateEnd.plusDays(1);
        }
        if (this.endDateEnd != null) {
            this.endDateEnd = this.endDateEnd.plusDays(1);
        }
    }
}