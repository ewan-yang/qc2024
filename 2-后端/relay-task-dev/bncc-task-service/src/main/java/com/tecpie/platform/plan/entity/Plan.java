package com.tecpie.platform.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.common.enums.PlanItemExecuteStatusEnum;
import com.tecpie.platform.plan_item.entity.PlanItem;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Getter
@Setter
@TableName("t_plan")
public class Plan extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划编号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 计划标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 计划所属年月
     */
    @TableField(value = "plan_month")
    private LocalDate planMonth;

    /**
     * 计划项List
     */
    @TableField(exist = false)
    private List<PlanItem> planItemList;

    /**
     * 总数
     */
    @TableField(exist = false)
    private int total;

    /**
     * 未关联数量
     */
    @TableField(exist = false)
    private int unassociatedQty;

    /**
     * 已关联数量
     */
    @TableField(exist = false)
    private int associated;

    public void statusQty(List<PlanItem> planItemList) {
        if (CollectionUtils.isEmpty(planItemList)) {
            return;
        }
        planItemList.forEach(t -> {
            String executeStatus = t.getExecuteStatus();
            if (PlanItemExecuteStatusEnum.WGL.getCode().equals(executeStatus)) {
                this.setUnassociatedQty(this.getUnassociatedQty() + 1);
            } else if (PlanItemExecuteStatusEnum.YGL.getCode().equals(executeStatus)) {
                this.setAssociated(this.getAssociated() + 1);
            }
        });
        this.setTotal(planItemList.size());
    }

}
