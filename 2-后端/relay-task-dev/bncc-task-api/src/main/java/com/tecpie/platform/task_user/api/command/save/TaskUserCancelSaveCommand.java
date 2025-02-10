package com.tecpie.platform.task_user.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户取消 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户取消请求参数")
@Getter
@Setter
public class TaskUserCancelSaveCommand {

  /**
   * 任务通知用户IdList
   */
  @Schema(description = "任务通知用户IdList, 可以批量", required = true)
  @NotNull(message = "taskUserIdList[任务通知用户IdList]不能为空")
  private List<Serializable> taskUserIdList;

  /**
   * 取消原因
   */
  @Schema(description = "取消原因")
  private String cancelReason;

}