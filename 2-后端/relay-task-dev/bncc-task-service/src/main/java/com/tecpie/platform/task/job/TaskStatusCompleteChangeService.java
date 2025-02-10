package com.tecpie.platform.task.job;

import cn.hutool.core.convert.Convert;
import com.tecpie.platform.common.enums.TaskExecuteStatusEnum;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 已反馈的停电任务变成已完成  定时任务
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 18:40
 */
@Slf4j
@Component
public class TaskStatusCompleteChangeService {

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    private final TaskService taskService;

    public TaskStatusCompleteChangeService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void run() {
        // 如果是外网，不执行该任务
        if (systemContainer == 1){
            return;
        }
        log.info("======= 执行停电通知执行状态变更定时任务  开始  ======");
        TaskQueryCommand command = new TaskQueryCommand();
        // 已反馈
        command.setExecuteStatusList(Arrays.asList(Convert.toStrArray(TaskExecuteStatusEnum.YFK.getCode())));
        // 扫描一周的已反馈数据
        LocalDate preWeekDate = LocalDate.now().plusWeeks(-1);
        command.setStartDateBegin(preWeekDate);
        command.setStartDateEnd(LocalDate.now().plusDays(1));
        List<Task> taskList = taskService.searchExtensionPageList(command);
        if (!CollectionUtils.isEmpty(taskList)) {
            for (Task task : taskList) {
                // 当前时间如果大于等于停电时间
                if (!LocalDateTime.now().isBefore(task.getStartTime())) {
                    // 变更为已完成
                    taskService.changeTaskExecuteStatus(task.getId(), TaskExecuteStatusEnum.YWC.getCode());
                }
            }
        }
        log.info("======= 执行停电通知执行状态变更定时任务  结束  ======");
    }

}