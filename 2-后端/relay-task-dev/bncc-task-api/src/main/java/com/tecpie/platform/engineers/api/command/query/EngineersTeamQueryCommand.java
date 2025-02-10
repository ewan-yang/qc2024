package com.tecpie.platform.engineers.api.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工程队表 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Schema(description = "工程队表分页检索请求参数")
@Getter
@Setter
public class EngineersTeamQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

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

}