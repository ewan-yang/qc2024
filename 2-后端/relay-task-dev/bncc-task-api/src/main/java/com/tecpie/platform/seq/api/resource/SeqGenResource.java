package com.tecpie.platform.seq.api.resource;

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
 * 序列表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Schema(description = "序列表响应结果")
@Getter
@Setter
public class SeqGenResource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键ID
   */
  @Schema(description = "主键ID")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonSerialize(using = ToStringSerializer.class)
  private Serializable id;

  /**
   * 业务类型
   */
  @Schema(description = "业务类型")
  private String businessType;

  /**
   * 业务名称
   */
  @Schema(description = "业务名称")
  private String businessName;

  /**
   * 生成规则字符串
   */
  @Schema(description = "生成规则字符串")
  private String formatStr;

  /**
   * 编号
   */
  @Schema(description = "编号")
  private Integer code;

  /**
   * 步长
   */
  @Schema(description = "步长")
  private Integer step;

  /**
   * 是否每天重置为0
   */
  @Schema(description = "是否每天重置为0")
  private Integer isClear;

  /**
   * 重置后的初始值
   */
  @Schema(description = "重置后的初始值")
  private Integer initValue;

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