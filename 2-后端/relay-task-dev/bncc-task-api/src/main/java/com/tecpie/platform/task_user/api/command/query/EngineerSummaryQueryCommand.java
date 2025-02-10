package com.tecpie.platform.task_user.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@Schema(description = "移动平台-工程队-首页-数据汇总-检索条件")
public class EngineerSummaryQueryCommand {

    @Schema(description = "工程队id")
    private Serializable engineersTeamId;

    @Schema(description = "执行状态集合")
    private List<String> executeStatusList;

    @Schema(description = "开始日期时间")
    private LocalDateTime startDateTime;

    @Schema(description = "结束日期时间")
    private LocalDateTime endDateTime;

}
