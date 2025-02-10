package com.tecpie.platform.task_user.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.common.util.FileUtil;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.task.api.resource.EngineersSumResource;
import com.tecpie.platform.task_user.api.TaskUserRestApi;
import com.tecpie.platform.task_user.api.command.query.TaskUserPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserAssignSaveCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserCancelSaveCommand;
import com.tecpie.platform.task_user.api.resource.*;
import com.tecpie.platform.task_user.controller.assembler.resource.TaskUserResourceAssembler;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.excel.cell.style.TaskUserCellStyleStrategy;
import com.tecpie.platform.task_user.service.TaskUserService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 停电任务用户表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskUser")
public class TaskUserRestController extends BaseController<TaskUserService, TaskUser, TaskUserResource> implements TaskUserRestApi {

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    private final ExcelTransfer<TaskUserResource> excelTransfer;

    @Autowired
    public TaskUserRestController(ExcelTransfer<TaskUserResource> excelTransfer) {
        this.excelTransfer = excelTransfer;
    }

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('taskUser:view')")
    public Result<TaskUserResource> getTaskUser(@PathVariable("id") Serializable id) {
        TaskUser entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskUserResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskUserResource>> searchTaskUserList(@RequestBody TaskUserQueryCommand command) {
        // 当前用户所属工程队ID
        command.setEngineersTeamId(TaskUtil.getEngineersTeamId());
        List<TaskUser> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskUserResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<TaskUserResource>> searchTaskUserPage(@RequestBody TaskUserPageQueryCommand command) {
        // 开启分页切面
        if (StringUtils.isBlank(command.getOrderBy())) {
            command.setOrderBy("main.task_id desc, main.receipt_code");
        }
        TaskUserQueryCommand condition = command.getCondition();
        // 当前用户所属工程队ID
        if (systemContainer == 1) {
            condition.setEngineersTeamId(TaskUtil.getEngineersTeamId());
        }
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        // 查询分页数据
        List<TaskUser> entityList = this.baseService.searchExtensionPageList(condition);
        // 构造分页结果
        return Result.success(entityList, TaskUserResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 查询用户最新的反馈时间
     */
    @Override
    @PostMapping("/selectMaxFeedbackTime")
    public Result<Map<String, String>> selectMaxFeedbackTime(@RequestBody List<Serializable> taskUserIdList) {
        return Result.success(this.baseService.selectMaxFeedbackTime(taskUserIdList));
    }

    /**
     * 根据idList获取打印数据
     */
    @Override
    @PostMapping("/getPrintData")
    public Result<List<TaskUserPrintResource>> getPrintData(@RequestBody List<Serializable> taskUserIdList) {
        return Result.success(this.baseService.getPrintData(taskUserIdList));
    }

    /**
     * 根据查询条件获取一键打印数据
     */
    @PostMapping("/getOneTouchPrintData")
    @Operation(summary = "根据查询条件获取一键打印数据")
    public Result<List<TaskUserPrintResource>> getOneTouchPrintData(@RequestBody TaskUserQueryCommand command) {
        return Result.success(this.baseService.getPrintData(command));
    }

    /**
     * 根据ID删除停电任务用户表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteTaskUserById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除停电任务用户表数据
     */
    @Override
    @DeleteMapping("/batchDelete/{ids}")
    public Result<Integer> deleteBatchIds(@PathVariable String ids) {
        this.baseService.removeByIds(List.of(Convert.toLongArray(ids)));
        return Result.success();
    }

    /**
     * 批量停电通知用户派发
     */
    @Override
    @PutMapping("/batchAssign")
    public Result batchAssign(@Valid @RequestBody TaskUserAssignSaveCommand command) {
        this.baseService.batchAssign(command);
        return Result.success();
    }

    /**
     * 根据查询条件一键派发停电通知用户
     */
    @PutMapping("/oneTouchAssign")
    @Operation(summary = "根据查询条件一键派发停电通知用户")
    public Result oneTouchAssign(@RequestBody TaskUserQueryCommand command) {
        this.baseService.oneTouchAssign(command);
        return Result.success();
    }

    /**
     * 变更派发负责方
     */
    @Override
    @PutMapping("/changeEngineersTeam")
    public Result changeEngineersTeam(@Valid @RequestBody TaskUserAssignSaveCommand command) {
        this.baseService.changeEngineersTeam(command);
        return Result.success();
    }

    /**
     * 根据查询条件一键变更派发负责方
     */
    @PutMapping("/oneTouchChangeEngineersTeam")
    @Operation(summary = "根据查询条件一键变更派发负责方")
    public Result oneTouchChangeEngineersTeam(@RequestBody TaskUserQueryCommand command) {
        this.baseService.oneTouchChangeEngineersTeam(command);
        return Result.success();
    }

    /**
     * 取消派发
     */
    @Override
    @PutMapping("/cancel")
    public Result cancel(@Valid @RequestBody TaskUserCancelSaveCommand command) {
        this.baseService.cancel(command.getTaskUserIdList(), command.getCancelReason());
        return Result.success();
    }

    /**
     * 根据查询条件一键取消派发
     */
    @PutMapping("/oneTouchCancel")
    @Operation(summary = "根据查询条件一键取消派发")
    public Result oneTouchCancel(@RequestBody TaskUserQueryCommand command) {
        this.baseService.oneTouchCancel(command);
        return Result.success();
    }

    /**
     * 恢复派发，针对那些误操作取消的用户进行状态恢复
     */
    @Override
    @PutMapping("/recoverCancel/{id}")
    public Result recoverCancel(@PathVariable Serializable id) {
        this.baseService.recoverCancel(id);
        return Result.success();
    }

    /**
     * 停电任务用户统计信息
     */
    @Override
    @PostMapping("/total")
    public Result<TaskUserTotalResource> getTaskUserTotal(@RequestBody TaskUserTotalQueryCommand command) {
        return Result.success(this.baseService.getTaskUserTotal(command));
    }

    /**
     * 停电任务用户数据汇总
     */
    @Override
    @PostMapping("/sumTotal")
    public Result<TaskUserSumTotalResource> getTaskUserSumTotal(@RequestBody TaskUserTotalQueryCommand command) {
        return Result.success(this.baseService.getTaskUserSumTotal(command));
    }

    /**
     * 下载停电通知用户导入模版
     */
    @Operation(summary = "下载停电通知用户导入模版")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/task_user_template.xlsx");
            FileUtil.downloadFile(request, response, classPathResource.getFile(), false);
        } catch (Exception e) {
            log.error("下载停电通知用户导入模版异常", e);
        }
    }

    /**
     * 导入停电通知用户信息
     */
    @Operation(summary = "导入停电通知用户信息")
    @PostMapping("/import")
    public Result<Map<String, Object>> uploadTemplate(@RequestParam("file") MultipartFile file, @RequestParam("startTime") String startTime)
            throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        //跳过excel第一行的head
        List<List<Object>> list = reader.read(1, reader.getRowCount() - 1);
        return Result.success(this.baseService.importTaskUser(list, startTime));
    }

    @Operation(summary = "导出停电通知用户信息（PC端使用）")
    @PostMapping("/export")
    public void export(@RequestBody TaskUserQueryCommand command, HttpServletResponse response) {
        List<TaskUser> taskUserList = baseService.searchExtensionPageList(command);
        List<TaskUserResource> taskUserResources = TaskUserResourceAssembler.INSTANCE.parseList(taskUserList);
        List<String> fieldNameList = command.getFieldNameList();
        HorizontalCellStyleStrategy strategy = TaskUserCellStyleStrategy.getStyle();
        excelTransfer.exportExcel(response, taskUserResources, "TASK_USER", "sheet1", strategy, fieldNameList, TaskUserResource.class);
    }

    /**
     * 生成停电通知用户回执二维码
     */
    @Operation(summary = "生成停电通知用户回执二维码")
    @GetMapping("/genQrCode/{id}")
    public void genTaskQrCode(@PathVariable("id") Serializable id, HttpServletResponse response) throws IOException {
        BufferedInputStream bis = null;
        try {
            TaskUser taskUser = this.baseService.selectExtensionById(id);
            // 生成二维码
            bis = new BufferedInputStream(this.baseService.genTaskQrCode(taskUser));
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition",
                    "attachment;filename=" + URLEncoder.encode((taskUser.getReceiptCode() + ".jpg"), StandardCharsets.UTF_8));
            response.setHeader("Access-Control-Expose-Headers", "content-disposition");
            IOUtils.copy(bis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("下载二维码异常", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    @PostMapping("/engineersSummary")
    @Operation(summary = "移动平台-工程队-首页-数据汇总")
    public Result<EngineersSumResource> engineersSummary() {
        return Result.success(baseService.engineersSummary());
    }

    @Override
    protected TaskUserResource toResource(TaskUser entity) {
        return TaskUserResourceAssembler.INSTANCE.parse(entity);
    }

}