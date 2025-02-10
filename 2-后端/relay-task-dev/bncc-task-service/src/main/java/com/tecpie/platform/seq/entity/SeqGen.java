package com.tecpie.platform.seq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 序列表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Getter
@Setter
@TableName("t_seq_gen")
public class SeqGen extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 业务名称
     */
    @TableField(value = "business_name")
    private String businessName;

    /**
     * 生成规则字符串
     */
    @TableField(value = "format_str")
    private String formatStr;

    /**
     * 编号
     */
    @TableField(value = "code")
    private Integer code;

    /**
     * 步长
     */
    @TableField(value = "step")
    private Integer step;

    /**
     * 是否每天重置为0
     */
    @TableField(value = "is_clear")
    private Integer isClear;

    /**
     * 重置后的初始值
     */
    @TableField(value = "init_value")
    private Integer initValue;

}
