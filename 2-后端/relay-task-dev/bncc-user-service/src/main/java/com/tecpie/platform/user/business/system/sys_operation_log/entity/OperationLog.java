package com.tecpie.platform.user.business.system.sys_operation_log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表 实体类
 *
 * @author tecpie
 * @since 2023-12-09
 */
@Getter
@Setter
@TableName("sys_operation_log")
public class OperationLog extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * traceId
   */
  @TableField(value = "trace_id")
  private String traceId;

  /**
   * spanId
   */
  @TableField(value = "span_id")
  private String spanId;

  /**
   * 来源ip
   */
  @TableField(value = "remote_ip")
  private String remoteIp;

  /**
   * 请求路径
   */
  @TableField(value = "request_uri")
  private String requestUri;

  /**
   * 请求类型
   */
  @TableField(value = "requestMethod")
  private String requestMethod;

  /**
   * 请求数据类型
   */
  @TableField(value = "contentType")
  private String contentType;

  /**
   * 请求头
   */
  @TableField(value = "requestHeaders")
  private String requestHeaders;

  /**
   * 请求参数
   */
  @TableField(value = "requestParams")
  private String requestParams;

  /**
   * 请求内容
   */
  @TableField(value = "requestBody")
  private String requestBody;

  /**
   * 操作名称
   */
  @TableField(value = "operation")
  private String operation;

  /**
   * 响应编码
   */
  @TableField(value = "httpStatus")
  private String httpStatus;

  /**
   * 响应内容
   */
  @TableField(value = "responseBody")
  private String responseBody;

  /**
   * 请求处理时间
   */
  @TableField(value = "duration")
  private Long duration;

}
