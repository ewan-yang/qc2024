package com.tecpie.platform.report.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PowerCutRepeatDetail {

    /**
     * 停电通知id
     */
    private Serializable id;

    /**
     * 停电通知单编号
     */
    private String taskCode;

    /**
     * 停电时间
     */
    private LocalDateTime startTime;

}
