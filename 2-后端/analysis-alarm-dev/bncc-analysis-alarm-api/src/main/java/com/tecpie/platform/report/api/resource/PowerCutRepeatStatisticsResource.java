package com.tecpie.platform.report.api.resource;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 断电重复统计资源
 * 重复停电统计分析 数据响应结果
 *
 * @author zhijie.tan
 * @date 2023/09/20
 * @since 2023-08-03
 */
@Schema(description = "重复停电统计分析 数据响应结果")
@Getter
@Setter
public class PowerCutRepeatStatisticsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    @ExcelProperty(value = "台区")
    private String region;

    /**
     * 所属台区用户数量
     */
    @Schema(description = "所属台区用户数量")
    @ExcelProperty(value = "台区户数")
    private int regionUserCount;

    /**
     * 停电次数
     */
    @Schema(description = "停电次数")
    @ExcelProperty(value = "停电次数")
    private int powerCutCount;
}