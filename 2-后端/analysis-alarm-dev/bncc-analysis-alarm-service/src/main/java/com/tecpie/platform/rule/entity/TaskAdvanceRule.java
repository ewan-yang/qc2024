package com.tecpie.platform.rule.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 告警规则维护 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@TableName("t_task_advance_rule")
public class TaskAdvanceRule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则编号
     */
    @TableField(value = "rule_code")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 时长(天)
     */
    @TableField(value = "days")
    private Integer days;

    /**
     * 区别参数1
     */
    @TableField(value = "params1")
    private String params1;

    /**
     * 区别参数2
     */
    @TableField(value = "params2")
    private String params2;

    /**
     * 区别参数3
     */
    @TableField(value = "params3")
    private String params3;

}
