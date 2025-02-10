package com.tecpie.platform.task_user.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户派发 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户派发请求参数")
@Getter
@Setter
public class TaskUserAssignSaveCommand {

  /**
   * 任务通知用户IdList
   */
  @Schema(description = "任务通知用户IdList, 可以批量", required = true)
  @NotNull(message = "taskUserIdList[任务通知用户IdList]不能为空")
  private List<Serializable> taskUserIdList;

  /**
   * 派发负责方(工程队id)
   */
  @Schema(description = "派发负责方(工程队id)")
  private Serializable engineersTeamId;

  /**
   * 类型：0或者null-PC端派发, 1-停电通知扫码派发，2-停电通知反馈，3-取消通知派发，4-取消通知反馈
   */
  @Schema(description = "类型：0或者null-PC端派发, 1-停电通知扫码派发，2-停电通知反馈，3-取消通知派发，4-取消通知反馈")
  private Integer type;

}