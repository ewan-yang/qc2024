package com.tecpie.platform.file.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件和图片表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Schema(description = "文件和图片表分页检索请求参数")
@Getter
@Setter
public class CommonFileQueryCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电任务模块，2-停电任务用户反馈模块
     */
    @Schema(description = "业务类型：1-停电任务模块，2-停电任务用户反馈模块")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @Schema(description = "业务数据ID，每个业务类型对应不同的表ID")
    private Serializable businessId;

}