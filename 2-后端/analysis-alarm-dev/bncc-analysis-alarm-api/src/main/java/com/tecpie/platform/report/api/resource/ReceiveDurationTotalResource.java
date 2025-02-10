package com.tecpie.platform.report.api.resource;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 接收时长统计分析 汇总响应结
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "接收时长统计分析 汇总响应结果")
@Getter
@Setter
public class ReceiveDurationTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时长小于4天
     */
    @Schema(description = "时长小于4天")
    private int hourLt4Days;

    /**
     * 时长4至7天
     */
    @Schema(description = "时长4至7天")
    private int hour4Z7Days;

    /**
     * 时长大于7天
     */
    @Schema(description = "时长大于7天")
    private int hourGt7Days;

    /**
     * 回执总数
     */
    @Schema(description = "回执总数")
    private int receiptCountQty;

    /**
     * 平均时长
     */
    @Schema(description = "平均时长")
    private String avgHour;

    /**
     * 各时段统计图
     */
    @Schema(description = "各时段统计图")
    private Map<String, Integer> timeIntervalMap;

    /**
     * 各区域平均接收时长统计分析图
     */
    @Schema(description = "各区域平均接收时长统计分析图")
    private Map<String, Map<String, Integer>> regionTimeMap;

    /**
     * 各用户类型平均接收时长统计分析图
     */
    @Schema(description = "各用户类型平均接收时长统计分析图")
    private Map<String, Map<String, Integer>> userTypeTimeMap;

    /**
     * 初始化数据
     */
    public void initData(List<ReceiveDurationStatisticsResource> dataList, Map<String, String> userTypeMap) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 回执总数
        this.setReceiptCountQty(dataList.size());
        // 总时长
        int sumHourTime = 0;
        // 总数量
        int sumCount = 0;
        // 各时段统计图
        Map<String, Integer> timeIntervalMap = Maps.newHashMap();
        // 各区域下达时长对比分析图
        Map<String, Map<String, Integer>> regionTimeMap = Maps.newLinkedHashMap();
        // 不同用户类型下达时长对比分分析图
        Map<String, Map<String, Integer>> userTypeTimeMap = Maps.newLinkedHashMap();
        // 循环数据
        for (ReceiveDurationStatisticsResource item : dataList) {
            // 所属台区
            String region = item.getRegion();
            Map<String, Integer> regionMap = null;
            if (StringUtils.isNotBlank(region)) {
                regionMap = regionTimeMap.get(region);
            }
            if (regionMap == null) {
                regionMap = Maps.newLinkedHashMap();
            }
            // 用户类型
            Integer userType = item.getUserType();
            Map<String, Integer> userTypeVlueMap = null;
            if (userType != null) {
                userTypeVlueMap = userTypeTimeMap.get(userType.toString());
            }
            if (userTypeVlueMap == null) {
                userTypeVlueMap = Maps.newLinkedHashMap();
            }
            // 间隔时长
            Integer hourDays = item.getHourDays();
            if (hourDays != null) {
                if (hourDays < 4) {
                    this.setHourLt4Days(this.getHourLt4Days() + 1);
                    timeIntervalMap.merge("时长小于4天", 1, Integer::sum);
                    regionMap.merge("时长小于4天", 1, Integer::sum);
                    userTypeVlueMap.merge("时长小于4天", 1, Integer::sum);
                } else if (hourDays <= 7) {
                    this.setHour4Z7Days(this.getHour4Z7Days() + 1);
                    timeIntervalMap.merge("时长4至7天", 1, Integer::sum);
                    regionMap.merge("时长4至7天", 1, Integer::sum);
                    userTypeVlueMap.merge("时长4至7天", 1, Integer::sum);
                } else {
                    this.setHourGt7Days(this.getHourGt7Days() + 1);
                    timeIntervalMap.merge("时长大于7天", 1, Integer::sum);
                    regionMap.merge("时长大于7天", 1, Integer::sum);
                    userTypeVlueMap.merge("时长大于7天", 1, Integer::sum);
                }
                if (StringUtils.isNotBlank(region)) {
                    regionTimeMap.put(region, regionMap);
                }
                if (userType != null) {
                    userTypeVlueMap.merge("回执个数", 1, Integer::sum);
                    userTypeTimeMap.put(userType.toString(), userTypeVlueMap);
                }
                // 总时长
                sumHourTime += hourDays;
                // 总数量
                sumCount += 1;
            }
        }
        // 计算 平均时长 = 总时长 / 数量
        if (sumCount > 0) {
            // 格式化小数
            DecimalFormat df = new DecimalFormat("0.0");
            double doubleValue = (double) sumHourTime / sumCount;
            this.setAvgHour(df.format(doubleValue));
        }
        this.setTimeIntervalMap(timeIntervalMap);
        this.setRegionTimeMap(regionTimeMap);
        // 格式化用户类型中文
        Map<String, Map<String, Integer>> userTypeCnTimeMap = Maps.newLinkedHashMap();
        for (String userType : userTypeTimeMap.keySet()) {
            String userTypeCn = userTypeMap.get(userType);
            userTypeCnTimeMap.put(userTypeCn, userTypeTimeMap.get(userType));
        }
        this.setUserTypeTimeMap(userTypeCnTimeMap);
    }

}