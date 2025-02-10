package com.tecpie.platform.task_repeat.api.resource;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 停电任务用户重复统计 实体类
 *
 * @author zhangyuheng
 * @since 2024-12-02
 */
@Getter
@Setter
public class TaskRepeatResource {

    // 通知单编号
    List<String> taskCodeList;

    // 户号
    List<String> accessNumberList;

    // 台区
    String region;

}
