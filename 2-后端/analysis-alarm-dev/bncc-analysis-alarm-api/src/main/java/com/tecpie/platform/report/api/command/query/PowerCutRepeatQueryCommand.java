package com.tecpie.platform.report.api.command.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 重复停电 列表检索请求参数
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Schema(description = "重复停电 检索请求参数")
@Getter
@Setter
public class PowerCutRepeatQueryCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电时间 起始
     */
    @Schema(description = "停电时间 起始")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateBegin;

    /**
     * 停电时间  截止
     */
    @Schema(description = "停电时间 截止")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDateEnd;

    @Schema(description = "线路名称")
    private String lineName;

    @Schema(description = "台区")
    private String region;

    /**
     * 初始化时间查询参数
     */
    public void initDateParam() {
        LocalDate currDateLocal = LocalDate.now();
        if (this.getStartDateBegin() == null) {
            this.setStartDateBegin(LocalDate.of(currDateLocal.getYear(), 1, 1));
        }
        if (this.getStartDateEnd() == null) {
            this.setStartDateEnd(currDateLocal);
        }
    }

}