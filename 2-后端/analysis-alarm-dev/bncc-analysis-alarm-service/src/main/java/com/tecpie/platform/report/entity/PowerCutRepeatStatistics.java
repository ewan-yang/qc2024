package com.tecpie.platform.report.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 重复停电数据统计 数据统计实体类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023-08-03
 */
@Getter
@Setter
public class PowerCutRepeatStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属台区
     */
    private String region;


    /**
     * 所属台区用户数量
     */
    private int regionUserCount;

    /**
     * 停电次数
     */
    private int powerCutCount;

}
