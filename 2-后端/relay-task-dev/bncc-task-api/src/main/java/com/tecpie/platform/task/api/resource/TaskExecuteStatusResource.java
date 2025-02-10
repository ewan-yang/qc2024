package com.tecpie.platform.task.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务执行状态表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务执行状态表响应结果")
@Getter
@Setter
public class TaskExecuteStatusResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键ID, 自增
   */
  @Schema(description = "主键ID, 自增")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 任务ID
   */
  @Schema(description = "任务ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable taskId;

  /**
   * 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
   */
  @Schema(description = "执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成")
  private String executeStatus;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  /**
   * 状态：0-停用，1-启用
   */
  @Schema(description = "状态：0-停用，1-启用")
  private Integer status;


  /**
   * 创建人
   */
  @Schema(description = "创建人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime createDate;

  /**
   * 修改人
   */
  @Schema(description = "修改人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 修改时间
   */
  @Schema(description = "修改时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

}