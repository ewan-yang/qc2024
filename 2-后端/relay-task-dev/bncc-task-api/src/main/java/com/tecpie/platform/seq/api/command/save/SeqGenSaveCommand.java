package com.tecpie.platform.seq.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 序列表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Schema(description = "序列表保存请求参数")
@Getter
@Setter
public class SeqGenSaveCommand {

  /**
   * 业务类型
   */
  @Schema(description = "业务类型", required = true)
  @NotBlank(message = "businessType[业务类型]不能为空")
  private String businessType;

  /**
   * 业务名称
   */
  @Schema(description = "业务名称", required = true)
  @NotBlank(message = "businessName[业务名称]不能为空")
  private String businessName;

  /**
   * 生成规则字符串
   */
  @Schema(description = "生成规则字符串", required = true)
  @NotBlank(message = "formatStr[生成规则字符串]不能为空")
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

}