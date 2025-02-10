package com.tecpie.platform.engineers.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工程队表 更新请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Schema(description = "工程队表更新请求参数")
@Getter
@Setter
public class EngineersTeamUpdateCommand {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Serializable id;

    /**
     * 工程队名称
     */
    @Schema(description = "工程队名称")
    private String name;

    /**
     * 所属区域
     */
    @Schema(description = "所属区域")
    private String area;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}