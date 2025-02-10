package com.tecpie.platform.task.api.command.query;

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
 * 停电任务表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务表分页检索请求参数")
@Getter
@Setter
public class TaskQueryCommand implements Serializable {

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
     * 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
     */
    @Schema(description = "执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成")
    private List<String> executeStatusList;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    private String taskCode;

    /**
     * 关联的任务编号
     */
    @Schema(description = "关联的任务编号，这个条件会把taskCode匹配的也查询出来")
    private String assTaskCode;

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
     * 任务idList  in查询
     */
    @JsonIgnore
    private List<Serializable> taskIdList;

    /**
     * 工程队Id
     */
    @JsonIgnore
    private String engineersTeamId;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        if (this.startDateEnd != null) {
            this.startDateEnd = this.startDateEnd.plusDays(1);
        }
        if (this.endDateEnd != null) {
            this.endDateEnd = this.endDateEnd.plusDays(1);
        }
    }

}