package com.tecpie.platform.user.business.system.sys_operation_log.api.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表 响应结果
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Schema(description = "操作日志表响应结果")
@Getter
@Setter
public class OperationLogResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * traceId
   */
  @Schema(description = "traceId")
  private String traceId;

  /**
   * spanId
   */
  @Schema(description = "spanId")
  private String spanId;

  /**
   * 来源ip
   */
  @Schema(description = "来源ip")
  private String remoteIp;

  /**
   * 请求路径
   */
  @Schema(description = "请求路径")
  private String requestUri;

  /**
   * 请求类型
   */
  @Schema(description = "请求类型")
  private String requestMethod;

  /**
   * 请求数据类型
   */
  @Schema(description = "请求数据类型")
  private String contentType;

  /**
   * 请求头
   */
  @Schema(description = "请求头")
  private String requestHeaders;

  /**
   * 请求参数
   */
  @Schema(description = "请求参数")
  private String requestParams;

  /**
   * 请求内容
   */
  @Schema(description = "请求内容")
  private String requestBody;

  /**
   * 操作名称
   */
  @Schema(description = "操作名称")
  private String operation;

  /**
   * 响应编码
   */
  @Schema(description = "响应编码")
  private String httpStatus;

  /**
   * 响应内容
   */
  @Schema(description = "响应内容")
  private String responseBody;

  /**
   * 请求处理时间
   */
  @Schema(description = "请求处理时间")
  private Long duration;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

  /**
   * 状态 - 0:停用,1:启用
   */
  @Schema(description = "状态 - 0:停用,1:启用")
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
   * 最后更新人
   */
  @Schema(description = "最后更新人")
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable updateBy;

  /**
   * 最后更新时间
   */
  @Schema(description = "最后更新时间")
  @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
  private LocalDateTime updateDate;

}