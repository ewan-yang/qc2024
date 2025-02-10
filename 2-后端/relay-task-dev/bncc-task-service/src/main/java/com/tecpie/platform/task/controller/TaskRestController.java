package com.tecpie.platform.task.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.enums.TaskExecuteStatusEnum;
import com.tecpie.platform.common.feign.advanceRule.AdvanceRuleFeignClient;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import com.tecpie.platform.task.api.TaskRestApi;
import com.tecpie.platform.task.api.command.query.TaskPageQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskTotalQueryCommand;
import com.tecpie.platform.task.api.command.save.TaskOutSaveCommand;
import com.tecpie.platform.task.api.command.save.TaskSaveCommand;
import com.tecpie.platform.task.api.command.update.TaskCancelCommand;
import com.tecpie.platform.task.api.command.update.TaskUpdateCommand;
import com.tecpie.platform.task.api.resource.TaskResource;
import com.tecpie.platform.task.api.resource.TaskSumTotalResource;
import com.tecpie.platform.task.api.resource.TaskTotalResource;
import com.tecpie.platform.task.controller.assembler.command.save.TaskSaveCommandAssembler;
import com.tecpie.platform.task.controller.assembler.command.update.TaskUpdateCommandAssembler;
import com.tecpie.platform.task.controller.assembler.resource.TaskResourceAssembler;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.starter.feign.entity.SecurityRole;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 停电任务表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/task")
public class TaskRestController extends BaseController<TaskService, Task, TaskResource> implements TaskRestApi {

    private final AdvanceRuleFeignClient advanceRuleFeignClient;

    @Autowired
    public TaskRestController(AdvanceRuleFeignClient advanceRuleFeignClient) {
        this.advanceRuleFeignClient = advanceRuleFeignClient;
    }


    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<TaskResource> getTask(@PathVariable("id") Serializable id) {
        Task entity = this.baseService.selectExtensionById(id);
        // 计算超期时长
        // 判断
        Result<TaskAdvanceRuleResource> carrierReleaseRule = advanceRuleFeignClient.getCarrierReleaseRule();
        Integer days = carrierReleaseRule.getData().getDays();
        TaskResource resource = TaskResourceAssembler.INSTANCE.parse(entity);
        resource.calOverdueDuration(days);
        return Result.success(resource);
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<List<TaskResource>> searchTaskList(@RequestBody TaskQueryCommand command) {
        List<Task> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<PagedResult<TaskResource>> searchTaskPage(@RequestBody TaskPageQueryCommand command) {
        // 如果角色是管理员或者市南管理员，可以看全部通知单
        List<SecurityRole> roleList = TecpieSecurityUtil.getUser().getRoleList();
        if (CollectionUtils.isNotEmpty(roleList)) {
            List<String> roleCodeList = roleList.stream().map(SecurityRole::getCode).collect(Collectors.toList());
            if (!(roleCodeList.contains("admin")) && !(roleCodeList.contains("sn_admin"))) {
                command.getCondition().setEngineersTeamId(TaskUtil.getEngineersTeamIdStr());
            }
        }
        // 开启分页切面
        command.setOrderBy("main.task_code desc");
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        // 查询分页数据
        List<Task> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据任务ID查询相关版本数据，并按照版本排序
     */
    @Override
    @GetMapping("/searchVersionList/{id}")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<List<TaskResource>> searchVersionList(@PathVariable("id") Serializable id) {
        List<Task> taskList = this.baseService.searchVersionList(id);
        return Result.success(TaskResourceAssembler.INSTANCE.parseList(taskList));
    }

    /**
     * 保存停电任务表数据
     */
    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('task:add')")
    public Result saveTask(@Valid @RequestBody TaskSaveCommand command) {
        return Result.success(this.baseService.saveTask(TaskSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 验证用户数据是否有重复停电
     */
    @Operation(summary = "验证用户是否有重复停电(新增)")
    @PostMapping("/validSaveTask")
    public Result<Map<String, Object>> validSaveTask(@Valid @RequestBody TaskSaveCommand command) {
        return Result.success(this.baseService.validRepeatPowerCut(TaskSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新停电任务表数据
     */
    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('task:update')")
    public Result updateTaskById(@PathVariable("id") Serializable id, @Valid @RequestBody TaskUpdateCommand command) {
        return Result.success(this.baseService.updateTask(id, TaskUpdateCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 验证用户数据是否有重复停电
     */
    @Operation(summary = "验证用户是否有重复停电(更新)")
    @PostMapping("/validUpdateTask")
    public Result<Map<String, Object>> validUpdateTask(@Valid @RequestBody TaskUpdateCommand command) {
        return Result.success(this.baseService.validRepeatPowerCut(TaskUpdateCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID删除停电任务表数据
     */
    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('task:delete')")
    public Result deleteTaskById(@PathVariable("id") Serializable id) {
        this.baseService.deleteTask(TaskUtil.toList(id));
        return Result.success();
    }

    /**
     * 批量删除停电任务表数据
     */
    @Override
    @DeleteMapping("/batchDelete/{ids}")
    @PreAuthorize("hasAuthority('task:delete')")
    public Result deleteBatchIds(@PathVariable String ids) {
        this.baseService.deleteTask(TaskUtil.toList(ids));
        return Result.success();
    }

    /**
     * 批量取消停电任务
     */
    @Override
    @PutMapping("/endTask/{ids}")
    @PreAuthorize("hasAuthority('task:update')")
    public Result endTaskByIds(@PathVariable("ids") String ids) {
        this.baseService.changeTaskExecuteStatus(TaskUtil.toList(ids), TaskExecuteStatusEnum.YQX.getCode());
        return Result.success();
    }

    /**
     * 取消停电任务
     */
    @Override
    @PutMapping("/cancelTask")
    @PreAuthorize("hasAuthority('task:update')")
    public Result cancelTask(@Valid @RequestBody TaskCancelCommand taskCancelCommand) {
        this.baseService.cancelTask(taskCancelCommand);
        return Result.success();
    }

    /**
     * 撤销 取消停电任务
     */
    @Override
    @PutMapping("/revokeCancelTask")
    @PreAuthorize("hasAuthority('task:update')")
    public Result revokeCancelTask(@Valid @RequestBody TaskCancelCommand taskCancelCommand) {
        this.baseService.revokeCancelTask(taskCancelCommand);
        return Result.success();
    }

    /**
     * 批量上传附件
     */
    @Operation(summary = "批量上传附件")
    @PostMapping(value = "/batchUpload")
    public Result<List<CommonFile>> batchUpload(@RequestParam("fileList") MultipartFile[] fileList) {
        return Result.success(this.baseService.batchUpload(fileList));
    }

    /**
     * 停电任务统计信息
     */
    @Override
    @PostMapping("/total")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<TaskTotalResource> getTaskTotal(@RequestBody TaskTotalQueryCommand command) {
        return Result.success(this.baseService.getTaskTotal(command));
    }

    /**
     * 停电任务数据汇总
     */
    @Override
    @PostMapping("/sumTotal")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<TaskSumTotalResource> getTaskSumTotal(@RequestBody TaskTotalQueryCommand command) {
        return Result.success(this.baseService.getTaskSumTotal(command));
    }

    /**
     * 导出停电任务信息
     */
    @Operation(summary = "导出停电任务信息")
    @PostMapping(value = "/download")
    @PreAuthorize("hasAuthority('task:view')")
    public void download(@RequestBody TaskQueryCommand command, HttpServletResponse response) throws IOException {
        this.baseService.download(this.baseService.searchExtensionPageList(command), response);
    }

    @GetMapping("/powerTest")
    @Operation(summary = "鉴权测试接口")
    @PreAuthorize("hasAuthority('task:view')")
    public Result<Void> powerTest() {
        return Result.success();
    }

    @PostMapping("/out/add/batch")
    @Operation(summary = "外部接口，批量新增")
    public Result<Void> insertOrSaveBatch(@RequestBody List<TaskOutSaveCommand> taskOutSaveCommandList) {
        this.baseService.insertOrSaveBatch(taskOutSaveCommandList);
        return Result.success();
    }

    @Override
    protected TaskResource toResource(Task entity) {
        return TaskResourceAssembler.INSTANCE.parse(entity);
    }

}