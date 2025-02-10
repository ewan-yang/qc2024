package com.tecpie.platform.report.api.resource;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 重复停电统计分析 汇总响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "重复停电统计分析 汇总响应结果")
@Getter
@Setter
public class PowerCutRepeatTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当期重复停电区域数
     */
    @Schema(description = "当期重复停电区域数")
    private int powerCutRepeatRegionQty;

    /**
     * 去年同期重复停电区域数
     */
    @Schema(description = "去年同期重复停电区域数")
    private int preYearPowerCutRepeatRegionQty;

    /**
     * 停电区域重复数同比增长数
     */
    @Schema(description = "停电区域重复数同比增长数")
    private int powerCutRepeatRegionTbQty;

    /**
     * 停电区域重复数同比趋势
     */
    @Schema(description = "停电区域重复数同比趋势")
    private String powerCutRepeatRegionTb;

    /**
     * 重复停电次数的统计分析图
     */
    @Schema(description = "重复停电次数的统计分析图")
    private Map<String, Integer> powerCutRepeatCountMap;

    /**
     * 初始化数据
     */
    public void initData(List<PowerCutRepeatStatisticsResource> dataList, List<PowerCutRepeatStatisticsResource> preYearDataList) {
        // 当期重复停电区域数
        this.setPowerCutRepeatRegionQty((int) dataList.stream().filter(t -> t.getPowerCutCount() > 1).count());
        // 去年同期数据
        if (!CollectionUtils.isEmpty(preYearDataList)) {
            // 去年同期重复停电区域数
            this.setPreYearPowerCutRepeatRegionQty((int) preYearDataList.stream().filter(t -> t.getPowerCutCount() > 1).count());
            // 同比增长数量
            int tbCount = this.getPowerCutRepeatRegionQty() - preYearDataList.size();
            this.setPowerCutRepeatRegionTbQty(tbCount);
            // 格式化小数
            DecimalFormat df = new DecimalFormat("0.0");
            // 同比增长百分比
            this.setPowerCutRepeatRegionTb(df.format((double) tbCount / preYearDataList.size() * 100));
        }
        // 重复停电次数的统计分析图
        powerCutRepeatCountMap = Maps.newLinkedHashMap();
        for (PowerCutRepeatStatisticsResource item : dataList) {
            // 停电次数
            powerCutRepeatCountMap.merge(item.getPowerCutCount() + "次", 1, Integer::sum);
        }
        this.setPowerCutRepeatCountMap(powerCutRepeatCountMap);
    }

}