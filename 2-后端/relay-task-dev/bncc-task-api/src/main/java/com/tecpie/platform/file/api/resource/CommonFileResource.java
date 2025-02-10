package com.tecpie.platform.file.api.resource;

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
 * 文件和图片表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Schema(description = "文件和图片表响应结果")
@Getter
@Setter
public class CommonFileResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 业务类型：1-停电任务模块，2-停电任务用户反馈模块
     */
    @Schema(description = "业务类型：1-停电任务模块，2-停电任务用户反馈模块")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @Schema(description = "业务数据ID，每个业务类型对应不同的表ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable businessId;

    /**
     * 文件来源：1-外网，2-内网
     */
    @Schema(description = "文件来源：1-外网，2-内网")
    private Integer fileSource;

    /**
     * 图片或附件名称
     */
    @Schema(description = "图片或附件名称")
    private String fileName;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private String fileSize;

    /**
     * 文件上传时间
     */
    @Schema(description = "文件上传时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime fileTime;

    /**
     * 文件路径：比如上传到OSS服务器可以存路径
     */
    @Schema(description = "文件路径：比如上传到OSS服务器可以存路径")
    private String fileUrl;

    /**
     * 文件或图片字节码
     */
    @Schema(description = "文件或图片字节码")
    private byte[] fileContent;

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