package com.tecpie.platform.task_notice.api.command.save;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 停电任务通知公告表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务通知公告表保存请求参数")
@Getter
@Setter
public class TaskNoticeSaveCommand {

    /**
     * 类型：1-通知，2-代办
     */
    @Schema(description = "类型：1-通知，2-代办", required = true)
    @NotNull(message = "type[类型：1-通知，2-代办]不能为空")
    private Integer type;

    /**
     * 标题
     */
    @Schema(description = "标题", required = true)
    @NotBlank(message = "title[标题]不能为空")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容", required = true)
    @NotBlank(message = "content[内容]不能为空")
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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}