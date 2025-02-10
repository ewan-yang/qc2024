package com.tecpie.platform.task.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.file.api.resource.CommonFileResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务表响应结果")
@Getter
@Setter
public class TaskResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键ID, 自增
   */
  @Schema(description = "主键ID, 自增")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  @Schema(description = "外部id")
  private String outId;

  /**
   * 停电计划项ID
   */
  @Schema(description = "停电计划项ID")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable planItemId;

  /**
   * 任务编号
   */
  @Schema(description = "任务编号")
  private String taskCode;

  /**
   * 关联的任务编号
   */
  @Schema(description = "关联的任务编号")
  private String assTaskCode;

  /**
   * 单位名称
   */
  @Schema(description = "单位名称")
  private String unitName;

  /**
   * 停电类型：1-计划停电
   */
  @Schema(description = "停电类型：1-计划停电")
  private Integer type;

  /**
   * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
   */
  @Schema(description = "停电原因：1-设备检修，2-配合客户接入，3-配合市政过程")
  private Integer reason;

  /**
   * 停电时间
   */
  @Schema(description = "停电时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
  private LocalDateTime startTime;

  /**
   * 送电时间
   */
  @Schema(description = "送电时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
  private LocalDateTime endTime;

  /**
   * 变电站名称
   */
  @Schema(description = "变电站名称")
  private String stationName;

  /**
   * 线路名称
   */
  @Schema(description = "线路名称")
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
   * 任务派发用户Id
   */
  @Schema(description = "任务派发用户Id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long assignedBy;

  /**
   * 任务派发用户姓名
   */
  @Schema(description = "任务派发用户姓名")
  private String assignedByName;

  /**
   * 停电任务通知来源
   */
  @Schema(description = "停电任务通知来源")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long taskSource;

  /**
   * 取消原因
   */
  @Schema(description = "取消原因")
  private String cancelReason;

  /**
   * 取消时间
   */
  @Schema(description = "取消时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime cancelTime;

  /**
   * 未派发数量
   */
  @Schema(description = "未派发数量")
  private Integer unassignedQty;

  /**
   * 已派发数量
   */
  @Schema(description = "已派发数量")
  private Integer assignedQty;

  /**
   * 已取消数量
   */
  @Schema(description = "已取消数量")
  private Integer cancelledQty;

  /**
   * 拒签数量
   */
  @Schema(description = "拒签数量")
  private Integer rejectedQty;

  /**
   * 同意数量
   */
  @Schema(description = "同意数量")
  private Integer agreedQty;

  /**
   * 未签数量
   */
  @Schema(description = "未签数量")
  private Integer unsignedQty;

  /**
   * 取消通知未派发数量
   */
  @Schema(description = "取消通知未派发数量")
  private Integer cancelUnassignedQty;

  /**
   * 取消通知已派发数量
   */
  @Schema(description = "取消通知已派发数量")
  private Integer cancelAssignedQty;

  /**
   * 取消通知未签数量
   */
  @Schema(description = "取消通知未签数量")
  private Integer cancelUnsignedQty;

  /**
   * 取消通知同意数量
   */
  @Schema(description = "取消通知同意数量")
  private Integer cancelAgreedQty;

  /**
   * 执行状态Id
   */
  @Schema(description = "执行状态Id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable executeStatusId;

  /**
   * 告警状态：0-正常
   */
  @Schema(description = "告警状态：0-正常，1-下达超时告警，2-下达风险告警，3-用户拒签告警，4-重复停电预警，5-计划变更预警")
  private Integer advanceStatus;

  /**
   * 执行状态对象
   */
  @Schema(description = "执行状态对象")
  private TaskExecuteStatusResource taskExecuteStatus;

  /**
   * 版本
   */
  @Schema(description = "版本")
  private Long version;

  /**
   * 扩展属性1
   */
  @Schema(description = "扩展属性1")
  private String attribute1;

  /**
   * 扩展属性2
   */
  @Schema(description = "扩展属性2")
  private String attribute2;

  /**
   * 扩展属性3
   */
  @Schema(description = "扩展属性3")
  private String attribute3;

  /**
   * 确认时间
   */
  @Schema(description = "确认时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
  private LocalDateTime confirmTime;

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

  /**
   * 文件List
   */
  @Schema(description = "文件List")
  private List<CommonFileResource> commonFileList;

  @Schema(description = "工程名称")
  private String projectName;

  @Schema(description = "超期时长")
  private int overdueDuration;

  public void calOverdueDuration(Integer days) {
    if (days == null) {
      return;
    }
    if (this.startTime != null && this.confirmTime != null) {
      int daysNum = (int) (this.startTime.toLocalDate().toEpochDay() - this.confirmTime.toLocalDate().toEpochDay());
      this.setOverdueDuration(days - daysNum);
    }
  }
}