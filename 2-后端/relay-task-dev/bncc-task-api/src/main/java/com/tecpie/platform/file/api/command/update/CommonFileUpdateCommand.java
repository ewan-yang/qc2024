package com.tecpie.platform.file.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件和图片表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Schema(description = "文件和图片表更新请求参数")
@Getter
@Setter
public class CommonFileUpdateCommand {

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    private Serializable id;

    /**
     * 图片或附件名称
     */
    @Schema(description = "图片或附件名称")
    private String fileName;

}