package com.tecpie.platform.user.security.igw.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * i国网用户实体
 *
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @Author Tanzj
 * @Date 2023/06/01
 */
@Getter
@Setter
public class IgwUser implements Serializable {

    private static final long serialVersionUID = -6247798498796940914L;

    private String nameCode;

    private String name;

}
