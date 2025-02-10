package com.tecpie.platform.file.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.hutool.core.date.DatePattern;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 文件和图片表 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Schema(description = "文件和图片表保存请求参数")
@Getter
@Setter
public class CommonFileSaveCommand {

  /**
   * 业务类型：1-停电任务模块，2-停电任务用户反馈模块
   */
  @Schema(description = "业务类型：1-停电任务模块，2-停电任务用户反馈模块", required = true)
  @NotNull(message = "businessType[业务类型]不能为空")
  private Integer businessType;

  /**
   * 业务数据ID，每个业务类型对应不同的表ID
   */
  @Schema(description = "业务数据ID，每个业务类型对应不同的表ID", required = true)
  @NotNull(message = "businessId[业务数据ID]不能为空")
  private Serializable businessId;

  /**
   * 文件路径：比如上传到OSS服务器可以存路径
   */
  @Schema(description = "文件路径：比如上传到OSS服务器可以存路径")
  private String fileUrl;

  /**
   * 备注
   */
  @Schema(description = "备注")
  private String remark;

}