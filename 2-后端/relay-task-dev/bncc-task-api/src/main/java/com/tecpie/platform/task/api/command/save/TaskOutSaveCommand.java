package com.tecpie.platform.task.api.command.save;

import com.tecpie.platform.task_user.api.command.save.TaskUserOutSaveCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskOutSaveCommand {

    @Schema(description = "外部id")
    private String outId;

    @Schema(description = "停电原因：设备检修，配合客户接入，配合市政过程")
    private String reason;

    @Schema(description = "停电时间")
    private LocalDateTime startTime;

    @Schema(description = "送电时间")
    private LocalDateTime endTime;

    @Schema(description = "变电站名称")
    private String stationName;

    @Schema(description = "线路名称")
    private String lineName;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "停电范围")
    private String ranges;

    @Schema(description = "停电位置")
    private String location;

    @Schema(description = "工作内容")
    private String jobContent;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "停电任务通知用户")
    private List<TaskUserOutSaveCommand> taskUserOutSaveCommandList;

}
