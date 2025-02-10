package com.tecpie.platform.task_user.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.task_user.api.TaskUserFeedbackLogRestApi;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFeedbackSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserFeedbackLogResource;
import com.tecpie.platform.task_user.controller.assembler.command.save.TaskUserFeedbackLogSaveCommandAssembler;
import com.tecpie.platform.task_user.controller.assembler.resource.TaskUserFeedbackLogResourceAssembler;
import com.tecpie.platform.task_user.entity.TaskUserFeedbackLog;
import com.tecpie.platform.task_user.service.TaskUserFeedbackLogService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskUserFeedbackLog")
public class TaskUserFeedbackLogRestController extends BaseController<TaskUserFeedbackLogService, TaskUserFeedbackLog, TaskUserFeedbackLogResource> implements TaskUserFeedbackLogRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<TaskUserFeedbackLogResource> getTaskUserFeedbackLog(@PathVariable("id") Serializable id) {
        TaskUserFeedbackLog entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskUserFeedbackLogResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskUserFeedbackLogResource>> searchTaskUserFeedbackLogList(@RequestBody TaskUserFeedbackLogQueryCommand command) {
        List<TaskUserFeedbackLog> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskUserFeedbackLogResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据停电用户ID获取反馈记录
     */
    @Override
    @GetMapping("/list/{taskUserId}")
    public Result<List<TaskUserFeedbackLogResource>> searchListByTaskUserId(@PathVariable("taskUserId") Serializable taskUserId) {
        List<TaskUserFeedbackLog> entityList = this.baseService.searchListByTaskUserId(taskUserId);
        return Result.success(TaskUserFeedbackLogResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<TaskUserFeedbackLogResource>> searchTaskUserFeedbackLogPage(@RequestBody TaskUserFeedbackLogPageQueryCommand command) {
        // 开启分页切面
        command.setOrderBy("main.create_date desc");
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        // 查询分页数据
        List<TaskUserFeedbackLog> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskUserFeedbackLogResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存停电任务用户反馈表数据
     */
    @Override
    @PostMapping
    public Result saveTaskUserFeedback(@RequestBody TaskUserFeedbackSaveCommand command) {
        return Result.success(this.baseService.saveTaskUserFeedbackLog(TaskUserFeedbackLogSaveCommandAssembler.INSTANCE.parse(command)));
    }

    @Operation(summary = "一键反馈")
    @PostMapping("/oneTouch")
    public Result<Void> oneTouchFeedback(@RequestBody TaskUserFeedbackSaveCommand command){
        this.baseService.oneTouchFeedback(command);
        return Result.success();
    }

    /**
     * 批量上传反馈图片
     */
    @Operation(summary = "批量上传反馈图片")
    @PostMapping(value = "/batchUpload")
    public Result<List<CommonFile>> batchUpload(@RequestParam("fileList") MultipartFile[] fileList) {
        return Result.success(this.baseService.batchUpload(fileList));
    }

    @Override
    protected TaskUserFeedbackLogResource toResource(TaskUserFeedbackLog entity) {
        return TaskUserFeedbackLogResourceAssembler.INSTANCE.parse(entity);
    }

}