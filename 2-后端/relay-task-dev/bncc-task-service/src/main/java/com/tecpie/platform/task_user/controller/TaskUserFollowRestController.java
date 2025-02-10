package com.tecpie.platform.task_user.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task_user.api.TaskUserFollowRestApi;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFollowSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserFollowResource;
import com.tecpie.platform.task_user.controller.assembler.command.save.TaskUserFollowSaveCommandAssembler;
import com.tecpie.platform.task_user.controller.assembler.resource.TaskUserFollowResourceAssembler;
import com.tecpie.platform.task_user.entity.TaskUserFollow;
import com.tecpie.platform.task_user.service.TaskUserFollowService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户跟进情况表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskUserFollow")
public class TaskUserFollowRestController extends BaseController<TaskUserFollowService, TaskUserFollow, TaskUserFollowResource> implements TaskUserFollowRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<TaskUserFollowResource> getTaskUserFollow(@PathVariable("id") Serializable id) {
        TaskUserFollow entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskUserFollowResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskUserFollowResource>> searchTaskUserFollowList(@RequestBody TaskUserFollowQueryCommand command) {
        List<TaskUserFollow> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskUserFollowResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<TaskUserFollowResource>> searchTaskUserFollowPage(@RequestBody TaskUserFollowPageQueryCommand command) {
        // 开启分页切面
        command.setOrderBy("main.create_date desc");
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        // 查询分页数据
        List<TaskUserFollow> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskUserFollowResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据停电用户ID获取跟进记录
     */
    @Override
    @GetMapping("/list/{taskUserId}")
    public Result<List<TaskUserFollowResource>> searchListByTaskUserId(@PathVariable("taskUserId") Serializable taskUserId) {
        List<TaskUserFollow> entityList = this.baseService.searchListByTaskUserId(taskUserId);
        return Result.success(TaskUserFollowResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据ID更新停电任务用户跟进表数据
     */
    @Override
    @PutMapping("/follow")
    public Result follow(@Valid @RequestBody TaskUserFollowSaveCommand command) {
        return Result.success(this.baseService.follow(TaskUserFollowSaveCommandAssembler.INSTANCE.parse(command)));
    }

    @Override
    protected TaskUserFollowResource toResource(TaskUserFollow entity) {
        return TaskUserFollowResourceAssembler.INSTANCE.parse(entity);
    }

}