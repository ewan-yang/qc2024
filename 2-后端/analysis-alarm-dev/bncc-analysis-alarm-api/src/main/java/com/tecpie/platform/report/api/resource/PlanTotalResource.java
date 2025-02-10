package com.tecpie.platform.report.api.resource;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 停电计划统计分析 汇总响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电计划统计分析 汇总响应结果")
@Getter
@Setter
public class PlanTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增计划数
     */
    @Schema(description = "新增计划数")
    private int planItemAddQty;

    /**
     * 变更计划数
     */
    @Schema(description = "变更计划数")
    private int planItemChangeQty;

    /**
     * 取消计划数
     */
    @Schema(description = "取消计划数")
    private int planItemCancelQty;

    /**
     * 计划总数
     */
    @Schema(description = "计划总数")
    private int planItemSumQty;

    /**
     * 各类型占比
     */
    @Schema(description = "各类型占比")
    private Map<String, Map<String, Double>> projectTypeZbMap;

    /**
     * 不同项目类型计划的新增情况统计
     */
    @Schema(description = "不同项目类型计划的新增情况统计")
    private Map<String, Map<String, Double>> projectTypeAddMap;

    /**
     * 不同项目类型计划的变更情况统计
     */
    @Schema(description = "不同项目类型计划的变更情况统计")
    private Map<String, Map<String, Double>> projectTypeChangeMap;

    /**
     * 不同项目类型计划的取消情况统计
     */
    @Schema(description = "不同项目类型计划的取消情况统计")
    private Map<String, Map<String, Double>> projectTypeCancelMap;

    /**
     * 初始化数据
     */
    public void initData(List<PlanStatisticsResource> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 计划总数
        this.setPlanItemSumQty(dataList.size());
        // 各类型占比
        Map<String, Map<String, Double>> projectTypeZbMap = Maps.newLinkedHashMap();
        // 不同项目类型计划的新增情况统计
        Map<String, Map<String, Double>> projectTypeAddMap = Maps.newLinkedHashMap();
        // 不同项目类型计划的变更情况统计
        Map<String, Map<String, Double>> projectTypeChangeMap = Maps.newLinkedHashMap();
        // 不同项目类型计划的取消情况统计
        Map<String, Map<String, Double>> projectTypeCancelMap = Maps.newLinkedHashMap();
        for (PlanStatisticsResource item : dataList) {
            // 项目类型
            String projectType = item.getProjectType();
            // 各类型数量和占比Map
            Map<String, Double> zbMap = projectTypeZbMap.get(projectType);
            if (ObjectUtils.isEmpty(zbMap)) {
                zbMap = Maps.newLinkedHashMap();
            }
            // 新增的数量和新增率Map
            Map<String, Double> addMap = projectTypeAddMap.get(projectType);
            if (ObjectUtils.isEmpty(addMap)) {
                addMap = Maps.newLinkedHashMap();
            }
            // 变更的数量和变更率Map
            Map<String, Double> changeMap = projectTypeChangeMap.get(projectType);
            if (ObjectUtils.isEmpty(changeMap)) {
                changeMap = Maps.newLinkedHashMap();
            }
            // 取消的数量和取消率Map
            Map<String, Double> cancelMap = projectTypeAddMap.get(projectType);
            if (ObjectUtils.isEmpty(cancelMap)) {
                cancelMap = Maps.newLinkedHashMap();
            }
            // 版本
            Long version = item.getVersion();
            if (version > 1) {
                this.setPlanItemChangeQty(this.getPlanItemChangeQty() + 1);
                changeMap.merge("qty", 1D, Double::sum);
            } else {
                this.setPlanItemAddQty(this.getPlanItemAddQty() + 1);
                addMap.merge("qty", 1D, Double::sum);
            }
            // 执行状态
            String executeStatus = item.getExecuteStatus();
            // 已关联
            if ("020".equals(executeStatus)) {
                this.setPlanItemCancelQty(this.getPlanItemCancelQty() + 1);
                cancelMap.merge("qty", 1D, Double::sum);
            }
            // 每个类型的数量
            zbMap.merge("qty", 1D, Double::sum);
            // 数量Map
            projectTypeZbMap.put(projectType, zbMap);
            projectTypeAddMap.put(projectType, addMap);
            projectTypeChangeMap.put(projectType, changeMap);
            projectTypeCancelMap.put(projectType, cancelMap);
        }
        // 格式化小数
        DecimalFormat df = new DecimalFormat("0.0");
        // 各类型占比
        for (String key : projectTypeZbMap.keySet()) {
            Map<String, Double> zbMap = projectTypeZbMap.get(key);
            if (zbMap.size() == 0) {
                zbMap.put("qty", 0.0);
                zbMap.put("zb", 0.0);
            } else {
                double zbValue = zbMap.get("qty") / this.getPlanItemSumQty() * 100;
                zbMap.put("zb", Double.valueOf(df.format(zbValue)));
            }
            projectTypeZbMap.put(key, zbMap);
        }
        // 各类型新增占比
        for (String key : projectTypeAddMap.keySet()) {
            Map<String, Double> addMap = projectTypeAddMap.get(key);
            if (addMap.size() == 0) {
                addMap.put("qty", 0.0);
                addMap.put("zb", 0.0);
            } else {
                double zbValue = addMap.get("qty") / this.getPlanItemAddQty() * 100;
                addMap.put("zb", Double.valueOf(df.format(zbValue)));
            }
            projectTypeAddMap.put(key, addMap);
        }
        // 各类型变更占比
        for (String key : projectTypeChangeMap.keySet()) {
            Map<String, Double> changeMap = projectTypeChangeMap.get(key);
            if (changeMap.size() == 0) {
                changeMap.put("qty", 0.0);
                changeMap.put("zb", 0.0);
            } else {
                double zbValue = changeMap.get("qty") / this.getPlanItemChangeQty() * 100;
                changeMap.put("zb", Double.valueOf(df.format(zbValue)));
            }
            projectTypeChangeMap.put(key, changeMap);
        }
        // 各类型取消占比
        for (String key : projectTypeCancelMap.keySet()) {
            Map<String, Double> cancelMap = projectTypeCancelMap.get(key);
            if (cancelMap.size() == 0) {
                cancelMap.put("qty", 0.0);
                cancelMap.put("zb", 0.0);
            } else {
                if (this.getPlanItemCancelQty() * 100 != 0){
                    double zbValue = cancelMap.get("qty") / this.getPlanItemCancelQty() * 100;
                    cancelMap.put("zb", Double.valueOf(df.format(zbValue)));
                }
            }
            projectTypeCancelMap.put(key, cancelMap);
        }
        this.setProjectTypeZbMap(projectTypeZbMap);
        this.setProjectTypeAddMap(projectTypeAddMap);
        this.setProjectTypeChangeMap(projectTypeChangeMap);
        this.setProjectTypeCancelMap(projectTypeCancelMap);
    }

}