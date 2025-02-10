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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知变更统计分析 汇总响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "通知变更统计分析 汇总响应结果")
@Getter
@Setter
public class TaskChangeTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知变更数量
     */
    @Schema(description = "通知变更数量")
    private int taskChangeQty;

    /**
     * 通知总数量
     */
    @Schema(description = "通知总数量")
    private int taskSumQty;

    /**
     * 去年同期通知变更数量
     */
    @Schema(description = "去年同期通知变更数量")
    private int preYearTaskChangeQty;

    /**
     * 去年同期通知总数量
     */
    @Schema(description = "去年同期通知总数量")
    private int preYearTaskSumQty;

    /**
     * 通知变更数同比趋势
     */
    @Schema(description = "通知变更数同比趋势")
    private double taskChangeTb;

    /**
     * 不同停电原因通知变更统计分析图
     */
    @Schema(description = "不同停电原因通知变更统计分析图")
    private Map<String, Map<String, Integer>> taskReasonChangeMap;

    /**
     * 不同来源通知变更统计分析图
     */
    @Schema(description = "不同来源通知变更统计分析图")
    private Map<String, Map<String, Integer>> taskSourceChangeMap;

    /**
     * 初始化数据
     */
    public void initData(List<TaskChangeStatisticsResource> dataList, List<TaskChangeStatisticsResource> preYearDataList, Map<String, String> userMap) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 通知总数
        this.setTaskSumQty(dataList.size());
        // 变更的通知数
        int taskChangeQty = 0;
        // 不同停电原因通知变更统计分析图
        Map<String, Map<String, Integer>> taskReasonChangeMap = Maps.newLinkedHashMap();
        // 不同来源通知变更统计分析图
        Map<String, Map<String, Integer>> taskSourceChangeMap = Maps.newLinkedHashMap();
        // 循环数据
        for (TaskChangeStatisticsResource item : dataList) {
            // 是否是变更数据  版本大于1的都是变更的数据
            boolean isChangeFlag = item.getVersion().intValue() > 1;
            // 停电原因
            Integer reason = item.getReason();
            Map<String, Integer> taskReasonMap = taskReasonChangeMap.get(reason.toString());
            if (taskReasonMap == null) {
                taskReasonMap = Maps.newHashMap();
            }
            // 不同来源通知变更统计分析图Map
            Serializable taskSource = item.getTaskSource();
            Map<String, Integer> taskSourceMap = null;
            if (ObjectUtils.isNotEmpty(taskSource)) {
                String userName = userMap.get(taskSource.toString());
                taskSourceMap = taskSourceChangeMap.get(userName);
            }
            if (taskSourceMap == null) {
                taskSourceMap = Maps.newHashMap();
            }
            // Y有变更 N未变更
            if (isChangeFlag) {
                taskSourceMap.merge("Y", 1, Integer::sum);
                taskReasonMap.merge("Y", 1, Integer::sum);
                // 变更数量
                taskChangeQty++;
            } else {
                taskSourceMap.merge("N", 1, Integer::sum);
                taskReasonMap.merge("N", 1, Integer::sum);
            }
            // 通知来源
            if (ObjectUtils.isNotEmpty(taskSource)) {
                String userName = userMap.get(taskSource.toString());
                taskSourceChangeMap.put(userName, taskSourceMap);
            }
            // 停电原因
            taskReasonChangeMap.put(reason.toString(), taskReasonMap);
        }
        this.setTaskChangeQty(taskChangeQty);
        this.setTaskReasonChangeMap(taskReasonChangeMap);
        this.setTaskSourceChangeMap(taskSourceChangeMap);
        // 去年同期通知总数量
        this.setPreYearTaskSumQty(preYearDataList.size());
        // 循环去年同期数据
        preYearDataList = preYearDataList.stream().filter(item -> item.getVersion().intValue() > 1).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(preYearDataList)) {
            this.setPreYearTaskChangeQty(preYearDataList.size());
        }
        if (preYearTaskChangeQty == 0) {
            taskChangeTb = 100;
        } else {
            taskChangeTb = BigDecimal.valueOf(taskChangeQty)
                    .subtract(BigDecimal.valueOf(preYearTaskChangeQty))
                    .divide(BigDecimal.valueOf(preYearTaskChangeQty), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
        }

    }

}