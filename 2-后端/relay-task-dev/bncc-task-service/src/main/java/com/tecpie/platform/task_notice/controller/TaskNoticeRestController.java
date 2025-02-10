package com.tecpie.platform.task_notice.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.feign.advance.AdvanceNoticeFeignClient;
import com.tecpie.platform.notice.api.command.query.BusinessAdvanceQueryCommand;
import com.tecpie.platform.notice.api.resource.AdvanceNoticeTotalResource;
import com.tecpie.platform.notice.api.resource.BusinessAdvanceNoticeResource;
import com.tecpie.platform.task_notice.api.TaskNoticeRestApi;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticePageQueryCommand;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticeQueryCommand;
import com.tecpie.platform.task_notice.api.command.save.TaskNoticeSaveCommand;
import com.tecpie.platform.task_notice.api.command.update.TaskNoticeUpdateCommand;
import com.tecpie.platform.task_notice.api.resource.TaskNoticeResource;
import com.tecpie.platform.task_notice.controller.assembler.command.save.TaskNoticeSaveCommandAssembler;
import com.tecpie.platform.task_notice.controller.assembler.command.update.TaskNoticeUpdateCommandAssembler;
import com.tecpie.platform.task_notice.controller.assembler.resource.TaskNoticeResourceAssembler;
import com.tecpie.platform.task_notice.entity.TaskNotice;
import com.tecpie.platform.task_notice.service.TaskNoticeService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 停电任务通知公告表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskNotice")
public class TaskNoticeRestController extends BaseController<TaskNoticeService, TaskNotice, TaskNoticeResource> implements TaskNoticeRestApi {

    @Autowired
    private AdvanceNoticeFeignClient advanceNoticeFeignClient;

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<TaskNoticeResource> getTaskNotice(@PathVariable("id") Serializable id) {
        TaskNotice entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskNoticeResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskNoticeResource>> searchTaskNoticeList(@RequestBody TaskNoticeQueryCommand command) {
        List<TaskNotice> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<TaskNoticeResource>> searchTaskNoticePage(@RequestBody TaskNoticePageQueryCommand command) {
        // 开启分页切面
        command.setOrderBy("main.create_date desc");
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        // 查询分页数据
        List<TaskNotice> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 查询当前用户所属的通知
     */
    @Override
    @PostMapping("/currUserTask/page")
    public Result<PagedResult<TaskNoticeResource>> searchCurrUserTaskNoticePage(@RequestBody TaskNoticePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskNotice.class, command));
        // 当前用户对应的角色IdList
        List<Long> roleIdList = TecpieSecurityUtil.getUser().getRoleList().stream().map(o -> o.getId()).collect(Collectors.toList());
        command.getCondition().setRoleIdList(roleIdList);
        // 查询分页数据
        List<TaskNotice> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存停电任务通知公告表数据
     */
    @Override
    @PostMapping
    public Result saveTaskNotice(@Valid @RequestBody TaskNoticeSaveCommand command) {
        this.baseService.saveTaskNotice(TaskNoticeSaveCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID更新停电任务通知公告表数据
     */
    @Override
    @PutMapping("/{id}")
    public Result updateTaskNoticeById(@PathVariable("id") Serializable id, @Valid @RequestBody TaskNoticeUpdateCommand command) {
        this.baseService.updateTaskNotice(id, TaskNoticeUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除停电任务通知公告表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteTaskNoticeById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        return Result.success();
    }

    /**
     * 通知读取
     */
    @Override
    @GetMapping("/read/{id}")
    public Result read(@PathVariable("id") Serializable id) {
        this.baseService.read(id);
        return Result.success();
    }

    /**
     * 根据ID变更停电任务通知公告表状态
     */
    @Override
    @PutMapping("/{id}/status/{status}")
    public Result changeTaskNoticeStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
        this.baseService.changeTaskNoticeStatus(id, status);
        return Result.success();
    }

    @PostMapping("/selectAlarmTotal")
    @Operation(summary = "预警数据统计")
    public Result<AdvanceNoticeTotalResource> selectAlarmTotal() {
        return advanceNoticeFeignClient.selectAlarmTotal();
    }

    @PostMapping("/selectAlarmBusinessList")
    @Operation(summary = "查询业务告警数据")
    public Result<List<BusinessAdvanceNoticeResource>> selectAlarmBusinessList(@RequestBody BusinessAdvanceQueryCommand command) {
        return advanceNoticeFeignClient.selectAlarmBusinessList(command);
    }


    @Override
    protected TaskNoticeResource toResource(TaskNotice entity) {
        return TaskNoticeResourceAssembler.INSTANCE.parse(entity);
    }

}