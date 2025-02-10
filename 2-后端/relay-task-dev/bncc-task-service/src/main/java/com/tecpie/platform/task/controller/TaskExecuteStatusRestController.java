package com.tecpie.platform.task.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task.api.TaskExecuteStatusRestApi;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusPageQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusQueryCommand;
import com.tecpie.platform.task.api.resource.TaskExecuteStatusResource;
import com.tecpie.platform.task.controller.assembler.resource.TaskExecuteStatusResourceAssembler;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import com.tecpie.platform.task.service.TaskExecuteStatusService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务执行状态表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskExecuteStatus")
public class TaskExecuteStatusRestController extends BaseController<TaskExecuteStatusService, TaskExecuteStatus, TaskExecuteStatusResource> implements TaskExecuteStatusRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<TaskExecuteStatusResource> getTaskExecuteStatus(@PathVariable("id") Serializable id) {
        TaskExecuteStatus entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskExecuteStatusResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskExecuteStatusResource>> searchTaskExecuteStatusList(@RequestBody TaskExecuteStatusQueryCommand command) {
        List<TaskExecuteStatus> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskExecuteStatusResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<TaskExecuteStatusResource>> searchTaskExecuteStatusPage(@RequestBody TaskExecuteStatusPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskExecuteStatus.class, command));
        // 查询分页数据
        List<TaskExecuteStatus> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskExecuteStatusResourceAssembler.INSTANCE.parseList(entityList));
    }

    @Override
    protected TaskExecuteStatusResource toResource(TaskExecuteStatus entity) {
        return TaskExecuteStatusResourceAssembler.INSTANCE.parse(entity);
    }

}