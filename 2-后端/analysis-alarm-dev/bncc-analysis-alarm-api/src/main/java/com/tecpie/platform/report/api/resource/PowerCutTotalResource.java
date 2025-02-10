package com.tecpie.platform.report.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 停电统计分析 汇总响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电统计分析 汇总响应结果")
@Getter
@Setter
public class PowerCutTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当期停电区域数
     */
    @Schema(description = "当期停电区域数")
    private int powerCutRegionQty;

    /**
     * 去年同期停电区域数
     */
    @Schema(description = "去年同期停电区域数")
    private int preYearPowerCutRegionQty;

    /**
     * 停电区域数同比趋势
     */
    @Schema(description = "停电区域数同比趋势")
    private String powerCutRegionTb = "100";

    /**
     * 不同停电原因的同环比分析图
     */
    @Schema(description = "不同停电原因的同环比分析图")
    private Map<String, Map<String, String>> reasonThbMap;

    /**
     * 上个月停电区域数量
     */
    @Schema(description = "上个月停电区域数量")
    @JsonIgnore
    private int preMonthPowerCutRegionQty;

    /**
     * 初始化数据
     */
    public void initData(List<PowerCutStatisticsResource> dataList,
                         List<PowerCutStatisticsResource> preYearDataList,
                         List<PowerCutStatisticsResource> lastMonthDataList,
                         Map<String, String> taskReasonMap) {
        // 不同停电原因的同环比分析图
        Map<String, Map<String, Integer>> reasonThbMap = Maps.newLinkedHashMap();
        // 当前同环比数据
        this.setThbMap(dataList, "currYear", reasonThbMap);
        // 去年同期数据
        if (!CollectionUtils.isEmpty(preYearDataList)) {
            this.setThbMap(preYearDataList, "preYear", reasonThbMap);
        }
        // 上个月数据
        if (!CollectionUtils.isEmpty(lastMonthDataList)) {
            this.setThbMap(lastMonthDataList, "preMonth", reasonThbMap);
        }
        // 格式化小数
        DecimalFormat df = new DecimalFormat("0.0");
        // 格式化停电原因中文
        Map<String, Map<String, String>> reasonCnTimeMap = Maps.newLinkedHashMap();
        for (String reason : reasonThbMap.keySet()) {
            Map<String, Integer> thbMap = reasonThbMap.get(reason);
            // 新的Map
            Map<String, String> valueMap = Maps.newLinkedHashMap();
            // 本期
            Integer currYearRegionQty = thbMap.get("currYearRegionQty");
            // 本年区域数量
            if (currYearRegionQty == null) {
                currYearRegionQty = 0;
            }
            valueMap.put("currYearRegionQty", String.valueOf(currYearRegionQty));
            // 去年区域数量
            valueMap.put("preYearRegionQty", "0");
            // 环比
            valueMap.put("regionQtyHb", "0");
            // 去年同期
            Integer preYearRegionQty = thbMap.get("preYearRegionQty");
            if (preYearRegionQty != null && preYearRegionQty > 0) {
                valueMap.put("preYearRegionQty", preYearRegionQty.toString());
            }
            // 计算环比 环比 =（本期数 - 上期数）/ 上期数 × 100%
            Integer preMonthRegionQty = thbMap.get("preMonthRegionQty");
            if (preMonthRegionQty != null && preMonthRegionQty > 0) {
                double value = (double) (currYearRegionQty - preMonthRegionQty) / preMonthRegionQty * 100;
                valueMap.put("regionQtyHb", df.format(value));
            }
            reasonCnTimeMap.put(taskReasonMap.get(reason), valueMap);
        }
        this.setReasonThbMap(reasonCnTimeMap);
    }

    private void setThbMap(List<PowerCutStatisticsResource> dataList, String key, Map<String, Map<String, Integer>> reasonThbMap) {
        // 区域去重
        List<String> regionSet = Lists.newArrayList();
        for (PowerCutStatisticsResource item : dataList) {
            if (regionSet.contains(item.getRegion())) {
                continue;
            }
            regionSet.add(item.getRegion());
            // 停电原因
            String reason = item.getReason().toString();
            // 同环比Map
            Map<String, Integer> thbMap = reasonThbMap.get(reason);
            if (thbMap == null) {
                thbMap = Maps.newLinkedHashMap();
            }
            thbMap.merge(key + "RegionQty", 1, Integer::sum);
            reasonThbMap.put(reason, thbMap);
        }
        if ("currYear".equals(key)) {
            this.setPowerCutRegionQty(regionSet.size());
        } else if ("preYear".equals(key)) {
            this.setPreYearPowerCutRegionQty(regionSet.size());
        } else if ("preMonth".equals(key)) {
            this.setPreMonthPowerCutRegionQty(regionSet.size());
        }
        if (preYearPowerCutRegionQty != 0) {
            this.setPowerCutRegionTb(String.valueOf(BigDecimal.valueOf(powerCutRegionQty)
                    .subtract(BigDecimal.valueOf(preYearPowerCutRegionQty))
                    .divide(BigDecimal.valueOf(preYearPowerCutRegionQty), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue()));
        }
    }
}