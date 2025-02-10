package com.tecpie.platform.user.business.system.sys_operation_log.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
/**
 * 操作日志表 更新请求参数
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Schema(description = "操作日志表更新请求参数")
@Getter
@Setter
public class OperationLogUpdateCommand {

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

}