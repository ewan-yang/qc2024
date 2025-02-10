package com.tecpie.platform.task_user.job;

import cn.hutool.core.convert.Convert;
import com.tecpie.platform.common.enums.TaskExecuteStatusEnum;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.service.TaskUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 下达超时告警  定时任务
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/22 18:40
 */
@Slf4j
@Component
public class TaskUserReleaseTimeOutAdvanceService {

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    private final TaskUserService taskUserService;

    public TaskUserReleaseTimeOutAdvanceService(TaskUserService taskUserService) {
        this.taskUserService = taskUserService;
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void run() {
        if (systemContainer == 1){
            return;
        }
        try {
            log.info("======= 执行停电通知用户下达超时告警定时任务  开始  ======");
            TaskUserQueryCommand command = new TaskUserQueryCommand();
            // 执行中的数据
            command.setTaskExecuteStatusList(Arrays.asList(Convert.toStrArray(TaskExecuteStatusEnum.ZXZ.getCode())));
            List<TaskUser> taskUserList = taskUserService.searchExtensionPageList(command);
            if (!CollectionUtils.isEmpty(taskUserList)) {
                // 下达超时告警
                taskUserService.releaseTimeOutAdvance(taskUserList);
            }
            log.info("======= 执行停电通知用户下达超时告警定时任务  结束  ======");
        } catch (Exception e) {
            log.info("======= 执行停电通知用户下达超时告警定时任务  失败  ======", e);
        }
    }

}