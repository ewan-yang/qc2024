package com.tecpie.platform.engineers.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工程队表 实体类
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Getter
@Setter
@TableName("t_engineers_team")
public class EngineersTeam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工程队名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 所属区域
     */
    @TableField(value = "area")
    private String area;

}
