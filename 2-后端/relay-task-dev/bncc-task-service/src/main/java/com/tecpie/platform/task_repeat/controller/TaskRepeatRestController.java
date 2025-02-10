package com.tecpie.platform.task_repeat.controller;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task_repeat.api.TaskRepeatRestApi;
import com.tecpie.platform.task_repeat.api.resource.TaskRepeatResource;
import com.tecpie.platform.task_repeat.service.TaskRepeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 停电任务用户表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskRepeat")
public class TaskRepeatRestController implements TaskRepeatRestApi {

    @Resource
    TaskRepeatService taskRepeatService;

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/all")
    public Result<List<TaskRepeatResource>> searchTaskRepeatAll() {
        List<TaskRepeatResource> taskRepeatList = taskRepeatService.searchTaskRepeatAll();
        return Result.success(taskRepeatList);
    }

    @Override
    @PostMapping("/{taskCode}")
    public Result<List<TaskRepeatResource>> searchTaskRepeatSingle(@PathVariable("taskCode") String taskCode) {
        List<TaskRepeatResource> taskRepeatList = taskRepeatService.searchTaskRepeatSingle(taskCode);
        return Result.success(taskRepeatList);
    }

    @Override
    @PostMapping("/update")
    public Result updateTaskRepeatAllOrSingle(@RequestBody TaskQueryCommand command) {
        taskRepeatService.updateTaskRepeatAllOrSingle(command.getTaskCode());
        return Result.success();
    }

}
