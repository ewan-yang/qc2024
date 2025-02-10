package com.tecpie.platform.task_notice.api.command.update;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务通知公告表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务通知公告表更新请求参数")
@Getter
@Setter
public class TaskNoticeUpdateCommand {

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    private Serializable id;

    /**
     * 类型：1-通知，2-代办
     */
    @Schema(description = "类型：1-通知，2-代办")
    private Integer type;

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
     * 预发布时间，只有当前时间大于等于这个时间的消息才会显示
     */
    @Schema(description = "预发布时间，只有当前时间大于等于这个时间的消息才会显示")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime pushTime;

    /**
     * 跳转的链接，这里一般是外链或前端路由地址
     */
    @Schema(description = "跳转的链接，这里一般是外链或前端路由地址")
    private String linkUrl;

    /**
     * 读取状态：0-未读，1-已读
     */
    @Schema(description = "读取状态：0-未读，1-已读")
    private Integer readStatus;

    /**
     * 读取时间
     */
    @Schema(description = "读取时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime readTime;

    /**
     * 读取用户ID
     */
    @Schema(description = "读取用户ID")
    private Long readUserId;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}