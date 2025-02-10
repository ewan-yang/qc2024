package com.tecpie.platform.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tecpie.library.common.business.entity.BaseEntity;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件和图片表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Getter
@Setter
@TableName("t_common_file")
public class CommonFile extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 业务类型：1-停电任务模块，2-停电任务用户反馈模块
   */
  @TableField(value = "business_type")
  private Integer businessType;

  /**
   * 业务数据ID，每个业务类型对应不同的表ID
   */
  @TableField(value = "business_id")
  private Serializable businessId;

  /**
   * 文件来源：1-外网，2-内网
   */
  @TableField(value = "file_source")
  private Integer fileSource;

  /**
   * 图片或附件名称
   */
  @TableField(value = "file_name")
  private String fileName;

  /**
   * 文件大小
   */
  @TableField(value = "file_size")
  private String fileSize;

  /**
   * 文件上传时间
   */
  @TableField(value = "file_time")
  private LocalDateTime fileTime;

  /**
   * 文件路径：比如上传到OSS服务器可以存路径
   */
  @TableField(value = "file_url")
  private String fileUrl;

  /**
   * 文件或图片字节码
   */
  @TableField(value = "file_content")
  private byte[] fileContent;

}
