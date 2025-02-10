package com.tecpie.platform.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 停电任务执行状态表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task_execute_status")
public class TaskExecuteStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    private Serializable taskId;

    /**
     * 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-已取消，161-已完成
     */
    @TableField(value = "execute_status")
    private String executeStatus;

}
