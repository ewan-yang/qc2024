package com.tecpie.platform.up_record.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 数据修改记录表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Getter
@Setter
@TableName("t_update_record")
public class UpdateRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-停电通知模块，2-后续新增业务再定义
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 业务数据ID，每个业务类型对应不同的表ID
     */
    @TableField(value = "business_id")
    private Long businessId;

    /**
     * 更新前版本json数据
     */
    @TableField(value = "pre_json")
    private String preJson;

    /**
     * 更新后版本json数据
     */
    @TableField(value = "after_json")
    private String afterJson;

    /**
     * 修改原因
     */
    @TableField(value = "reason")
    private String reason;

}
