package com.tecpie.platform.task.job;

import com.tecpie.platform.common.enums.TaskExecuteStatusEnum;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 不正常的停电任务变成已取消  定时任务
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 18:40
 */
@Slf4j
@Component
public class TaskStatusCancelChangeService {

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    private final TaskService taskService;

    public TaskStatusCancelChangeService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void run() {
        // 如果是外网，不执行该任务
        if (systemContainer == 1){
            return;
        }
        log.info("======= 执行停电通知执行状态过时间变更已取消定时任务  开始  ======");
        // 扫描一周的执行中的数据
        TaskQueryCommand command = new TaskQueryCommand();
        command.setExecuteStatusList(Collections.singletonList(TaskExecuteStatusEnum.ZXZ.getCode()));
        command.setStartDateBegin(LocalDate.now().plusWeeks(-1));
        command.setStartDateEnd(LocalDate.now());
        List<Task> taskList = taskService.searchExtensionPageList(command);
        if (!CollectionUtils.isEmpty(taskList)) {
            List<Serializable> taskIdList = Lists.newArrayList();
            for (Task task : taskList) {
                // 当前时间如果大于等于停电时间
                if (!LocalDateTime.now().isBefore(task.getStartTime())) {
                    // 变更为已取消
                    taskService.changeTaskExecuteStatus(task.getId(), TaskExecuteStatusEnum.YQX.getCode());
                    taskIdList.add(task.getId());
                }
            }
        }
        log.info("======= 执行停电通知执行状态过时间变更已取消定时任务  结束  ======");
    }

}