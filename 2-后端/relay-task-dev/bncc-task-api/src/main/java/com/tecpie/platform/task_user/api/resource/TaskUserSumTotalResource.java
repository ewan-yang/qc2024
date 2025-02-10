package com.tecpie.platform.task_user.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 停电任务用户汇总 统计响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户汇总 统计响应结果")
@Getter
@Setter
public class TaskUserSumTotalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电用户总数量
     */
    @Schema(description = "停电用户总数量")
    private Integer taskUserSumQty;

    /**
     * 停电通知总数量同比
     */
    @Schema(description = "停电通知总数量同比")
    private Integer taskUserSumTb;

    /**
     * 停电通知总数量同比百分比
     */
    @Schema(description = "停电通知总数量同比百分比, 如果返回null前端显示--")
    private Integer taskUserSumTbPercentage;

    /**
     * 停电区域数量
     */
    @Schema(description = "停电区域数量")
    private Integer powerCutAreaQty;

    /**
     * 停电区域数量同比
     */
    @Schema(description = "停电区域数量同比")
    private Integer powerCutAreaTb;

    /**
     * 停电区域数量同比百分比
     */
    @Schema(description = "停电区域数量同比百分比, 如果返回null前端显示--")
    private Integer powerCutAreaTbPercentage;

    /**
     * 重复停电区域数量
     */
    @Schema(description = "重复停电区域数量")
    private Integer powerCutRepeatAreaQty;

    /**
     * 重复停电区域数量同比
     */
    @Schema(description = "重复停电区域数量同比")
    private Integer powerCutRepeatAreaTb;

    /**
     * 重复停电区域数量同比百分比
     */
    @Schema(description = "重复停电区域数量同比百分比, 如果返回null前端显示--")
    private Integer powerCutRepeatAreaTbPercentage;

    /**
     * 停电用户数量同比、百分比
     *
     * @param preYearTaskUserQty 往年停电用户数量
     */
    public void calTaskUserTb(Integer preYearTaskUserQty) {
        if (this.taskUserSumQty == null || preYearTaskUserQty == null) {
            return;
        }
        // 同比增长数量
        int tbCount = this.taskUserSumQty - preYearTaskUserQty;
        this.setTaskUserSumTb(tbCount);
        // 同比增长百分比
        if (preYearTaskUserQty > 0) {
            this.setTaskUserSumTbPercentage(tbCount / preYearTaskUserQty * 100);
        }
    }

    /**
     * 计算停电区域数量同比、百分比
     *
     * @param preYearPowerCutAreaQty 往年停电区域数量
     */
    public void calPowerCutAreaTb(Integer preYearPowerCutAreaQty) {
        if (this.powerCutAreaQty == null || preYearPowerCutAreaQty == null) {
            return;
        }
        // 同比增长数量
        int tbCount = this.powerCutAreaQty - preYearPowerCutAreaQty;
        this.setPowerCutAreaTb(tbCount);
        // 同比增长百分比
        if (preYearPowerCutAreaQty > 0) {
            this.setPowerCutAreaTbPercentage(tbCount / preYearPowerCutAreaQty * 100);
        }
    }

    /**
     * 计算重复停电区域数量同比、百分比
     *
     * @param preYearPowerCutRepeatAreaQty 往年重复停电区域数量
     */
    public void calPowerCutRepeatAreaTb(Integer preYearPowerCutRepeatAreaQty) {
        if (this.powerCutRepeatAreaQty == null || preYearPowerCutRepeatAreaQty == null) {
            return;
        }
        // 同比增长数量
        int tbCount = this.powerCutRepeatAreaQty - preYearPowerCutRepeatAreaQty;
        this.setPowerCutRepeatAreaTb(tbCount);
        // 同比增长百分比
        if (preYearPowerCutRepeatAreaQty > 0) {
            this.setPowerCutRepeatAreaTbPercentage(tbCount / preYearPowerCutRepeatAreaQty * 100);
        }
    }

}