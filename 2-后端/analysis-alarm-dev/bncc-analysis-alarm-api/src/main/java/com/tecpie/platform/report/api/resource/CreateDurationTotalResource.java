package com.tecpie.platform.report.api.resource;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 创建时长统计分析 汇总响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "创建时长统计分析 汇总响应结果")
@Getter
@Setter
public class CreateDurationTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时长小于8天
     */
    @Schema(description = "时长小于8天")
    private int hourLt8Days;

    /**
     * 时长8至10天
     */
    @Schema(description = "时长8至10天")
    private int hour8Z10Days;

    /**
     * 时长11至15天
     */
    @Schema(description = "时长11至15天")
    private int hour11Z15Days;

    /**
     * 时长大于15天
     */
    @Schema(description = "时长大于15天")
    private int hourGt15Days;

    /**
     * 通知总数
     */
    @Schema(description = "通知总数")
    private int taskCountQty;

    /**
     * 平均时长
     */
    @Schema(description = "平均时长")
    private String avgHour;

    /**
     * 各个时段占比
     */
    @Schema(description = "各个时段占比")
    private Map<String, String> timeIntervalMap;

    /**
     * 各时长个数的统计分析图
     */
    @Schema(description = "各时长个数的统计分析图")
    private Map<Integer, Integer> daysTimeMap;

    /**
     * 不同来源的时长统计图
     */
    @Schema(description = "不同来源的时长统计图")
    private Map<String, Map<String, Integer>> taskSourceTimeMap;

    /**
     * 不同来源平均时长统计图
     */
    @Schema(description = "不同来源平均时长统计图")
    private Map<String, Double> taskSourceAvgTimeMap;

    /**
     * 初始化数据
     */
    public void initData(List<CreateDurationStatisticsResource> dataList, Map<String, String> userMap) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 通知总数
        this.setTaskCountQty(dataList.size());
        // 总时长
        int sumHourTime = 0;
        // 总数量
        int sumCount = 0;
        // 各时长个数的统计分析图
        Map<Integer, Integer> daysTimeMap = Maps.newTreeMap();
        // 不同来源的时长统计图
        Map<String, Map<String, Integer>> taskSourceTimeMap = Maps.newLinkedHashMap();
        // 循环数据
        for (CreateDurationStatisticsResource item : dataList) {
            // 通知来源对应的各类型时长Map
            Map<String, Integer> sourceTimeMap = null;
            // 通知来源
            Serializable taskSource = item.getTaskSource();
            if (ObjectUtils.isNotEmpty(taskSource)) {
                String userName = userMap.get(taskSource.toString());
                // 每个通知来源对应的各类型时长
                sourceTimeMap = taskSourceTimeMap.get(userName);
            }
            if (sourceTimeMap == null) {
                sourceTimeMap = Maps.newHashMap();
                sourceTimeMap.put("时长小于8天", 0);
                sourceTimeMap.put("时长8至10天", 0);
                sourceTimeMap.put("时长11至15天", 0);
                sourceTimeMap.put("时长大于15天", 0);
            }
            // 间隔天数
            Integer days = item.getDays();
            if (days != null) {
                if (days < 8) {
                    this.setHourLt8Days(this.getHourLt8Days() + 1);
                    // 时长值
                    sourceTimeMap.merge("时长小于8天", 1, Integer::sum);
                } else if (days <= 10) {
                    this.setHour8Z10Days(this.getHour8Z10Days() + 1);
                    // 时长值
                    sourceTimeMap.merge("时长8至10天", 1, Integer::sum);
                } else if (days <= 15) {
                    this.setHour11Z15Days(this.getHour11Z15Days() + 1);
                    // 时长值
                    sourceTimeMap.merge("时长11至15天", 1, Integer::sum);
                } else {
                    this.setHourGt15Days(this.getHourGt15Days() + 1);
                    // 时长值
                    sourceTimeMap.merge("时长大于15天", 1, Integer::sum);
                }
                // 不同来源总时长值
                sourceTimeMap.merge("sumHourDays", days, Integer::sum);
                if (ObjectUtils.isNotEmpty(taskSource)) {
                    String userName = userMap.get(taskSource.toString());
                    taskSourceTimeMap.put(userName, sourceTimeMap);
                }
                // 各时长个数的统计分析图
                daysTimeMap.merge(days, 1, Integer::sum);
                // 总时长
                sumHourTime += days;
                // 总数量
                sumCount += 1;
            }
        }
        // 各个时段占比
        Map<String, String> timeIntervalMap = Maps.newLinkedHashMap();
        // 格式化小数
        DecimalFormat df = new DecimalFormat("0.0");
        if (sumCount > 0) {
            // 计算 平均时长 = 总时长 / 数量
            double doubleValue = (double) sumHourTime / sumCount;
            this.setAvgHour(df.format(doubleValue));
            // 时长小于8天 占比
            doubleValue = (double) this.getHourLt8Days() / sumCount * 100;
            timeIntervalMap.put("时长小于8天", df.format(doubleValue));
            // 时长8至10天 占比
            doubleValue = (double) this.getHour8Z10Days() / sumCount * 100;
            timeIntervalMap.put("时长8至10天", df.format(doubleValue));
            // 时长11至15天 占比
            doubleValue = (double) this.getHour11Z15Days() / sumCount * 100;
            timeIntervalMap.put("时长11至15天", df.format(doubleValue));
            // 时长大于15天 占比
            doubleValue = (double) this.getHourGt15Days() / sumCount * 100;
            timeIntervalMap.put("时长大于15天", df.format(doubleValue));
        }
        // 不同来源平均时长统计图
        Map<String, Double> taskSourceAvgTimeMap = Maps.newHashMap();
        for (String userName : taskSourceTimeMap.keySet()) {
            Map<String, Integer> sourceTimeMap = taskSourceTimeMap.get(userName);
            // 当前来源总时长
            Integer sourceSumHourDays = sourceTimeMap.get("sumHourDays");
            // 数量
            int count = 0;
            for (Map.Entry<String, Integer> entry : sourceTimeMap.entrySet()) {
                if (!entry.getKey().equals("sumHourDays") && entry.getValue() != null) {
                    count += entry.getValue();
                }
            }
            if (sourceSumHourDays != null) {
                BigDecimal value = BigDecimal.valueOf(sourceSumHourDays).divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
                // 四舍五入为2位
                taskSourceAvgTimeMap.put(userName, value.doubleValue());
            }
            sourceTimeMap.remove("sumHourDays");
        }
        this.setTimeIntervalMap(timeIntervalMap);
        this.setDaysTimeMap(daysTimeMap);
        this.setTaskSourceTimeMap(taskSourceTimeMap);
        this.setTaskSourceAvgTimeMap(taskSourceAvgTimeMap);
    }

}