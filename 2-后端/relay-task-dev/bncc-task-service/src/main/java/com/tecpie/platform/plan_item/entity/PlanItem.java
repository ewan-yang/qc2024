package com.tecpie.platform.plan_item.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.plan.entity.Plan;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 停电计划项表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Getter
@Setter
@TableName("t_plan_item")
public class PlanItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 停电计划ID
     */
    @TableField(value = "plan_id")
    private Serializable planId;

    /**
     * 工程编号
     */
    @TableField(value = "project_code")
    private String projectCode;

    /**
     * 工程账号
     */
    @TableField(value = "project_account")
    private String projectAccount;

    /**
     * 工程名称
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 项目类型
     */
    @TableField(value = "project_type")
    private String projectType;

    /**
     * 停役时间
     */
    @TableField(value = "start_time")
    private LocalDate startTime;

    /**
     * 复役时间
     */
    @TableField(value = "end_time")
    private LocalDate endTime;

    /**
     * 变电站/线路名称
     */
    @TableField(value = "station_line_name")
    private String stationLineName;

    /**
     * 主要工作内容及相关验收部门		(注:仅供参考,请以设计图纸及现场实际工作内容为准)
     */
    @TableField(value = "job_content")
    private String jobContent;

    /**
     * 停役设备		(注:仅供参考,请以现场勘查后实际停役单为准)
     */
    @TableField(value = "cos_device")
    private String cosDevice;

    /**
     * 施工班组
     */
    @TableField(value = "construction_team")
    private String constructionTeam;

    /**
     * 执行状态：010-待执行，020-执行中，031-已完成，032-已取消，040-未执行
     * change : 2023/9/12   执行装袋修改为 010-未关联 020-已关联  其余状态删除
     */
    @TableField(value = "execute_status")
    private String executeStatus;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private LocalDate cancelTime;

    /**
     * 版本
     */
    @TableField(value = "version")
    private Long version;

    /**
     * 停电计划对象
     */
    @TableField(exist = false)
    private Plan plan;

    public void copy(PlanItem source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
