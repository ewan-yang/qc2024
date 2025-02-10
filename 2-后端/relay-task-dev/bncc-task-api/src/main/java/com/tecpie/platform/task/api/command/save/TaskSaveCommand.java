package com.tecpie.platform.task.api.command.save;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecpie.platform.file.api.command.update.CommonFileUpdateCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserSaveCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务表保存请求参数")
@Getter
@Setter
public class TaskSaveCommand {

    /**
     * 停电计划项ID
     */
    @Schema(description = "停电计划项ID")
    private Serializable planItemId;

    /**
     * 外部id（配网办id）
     */
    @Schema(description = "外部id（配网办id）")
    private String outId;

    /**
     * 停电类型：1-计划停电
     */
    @Schema(description = "停电类型：1-计划停电", required = true)
    @NotNull(message = "type[停电类型：1-计划停电]不能为空")
    private Integer type;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    @Schema(description = "停电原因：1-设备检修，2-配合客户接入，3-配合市政过程", required = true)
    @NotNull(message = "reason[停电原因：1-设备检修，2-配合客户接入，3-配合市政过程]不能为空")
    private Integer reason;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间", required = true)
    @NotNull(message = "startTime[停电时间]不能为空")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间", required = true)
    @NotNull(message = "endTime[送电时间]不能为空")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 变电站名称
     */
    @Schema(description = "变电站名称", required = true)
    @NotBlank(message = "stationName[变电站名称]不能为空")
    private String stationName;

    /**
     * 线路名称
     */
    @Schema(description = "线路名称", required = true)
    @NotBlank(message = "lineName[线路名称]不能为空")
    private String lineName;

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
     * 停电位置
     */
    @Schema(description = "停电位置")
    private String location;

    /**
     * 工作内容
     */
    @Schema(description = "工作内容")
    private String jobContent;

    /**
     * 是否通知媒体：0-否，1-是
     */
    @Schema(description = "是否通知媒体：0-否，1-是")
    private Integer boolNotifyMedia;

    /**
     * 停电任务通知来源
     */
    @Schema(description = "停电任务通知来源")
    private Long taskSource;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 停电任务通知用户List
     */
    @Schema(description = "停电任务通知用户List")
    private List<TaskUserSaveCommand> taskUserList;

    /**
     * 提交类型
     */
    @Schema(description = "提交类型：0-暂存，1-提交")
    private Integer submitType;

    /**
     * 图片List
     */
    @Schema(description = "图片List")
    private List<CommonFileUpdateCommand> commonFileList;
}