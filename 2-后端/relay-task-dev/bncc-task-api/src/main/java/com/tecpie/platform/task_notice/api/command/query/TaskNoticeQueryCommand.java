package com.tecpie.platform.task_notice.api.command.query;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务通知公告表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务通知公告表分页检索请求参数")
@Getter
@Setter
public class TaskNoticeQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 读取状态：0-未读，1-已读
     */
    @Schema(description = "读取状态：0-未读，1-已读")
    private Integer readStatus;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 创建开始时间
     */
    @Schema(description = "创建开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateBegin;

    /**
     * 创建结束时间
     */
    @Schema(description = "创建结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDateEnd;

    /**
     * 角色IdList  内部使用
     */
    @JsonIgnore
    private List<Long> roleIdList;

}