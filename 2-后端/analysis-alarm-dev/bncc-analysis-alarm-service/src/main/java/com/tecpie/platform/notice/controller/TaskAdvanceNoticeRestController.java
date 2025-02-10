package com.tecpie.platform.notice.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.notice.api.TaskAdvanceNoticeRestApi;
import com.tecpie.platform.notice.api.command.query.*;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.update.AdvanceNoticeStatusUpdateCommand;
import com.tecpie.platform.notice.api.resource.*;
import com.tecpie.platform.notice.controller.assembler.resource.*;
import com.tecpie.platform.notice.entity.*;
import com.tecpie.platform.notice.service.TaskAdvanceNoticeService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 预告警表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskAdvanceNotice")
public class TaskAdvanceNoticeRestController extends BaseController<TaskAdvanceNoticeService, TaskAdvanceNotice, TaskAdvanceNoticeResource> implements TaskAdvanceNoticeRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<TaskAdvanceNoticeResource> getTaskAdvanceNotice(@PathVariable("id") Serializable id) {
        return Result.success(toResource(this.baseService.selectExtensionById(id)));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<TaskAdvanceNoticeResource>> searchTaskAdvanceNoticeResourceList(@RequestBody TaskAdvanceNoticeQueryCommand command) {
        return Result.success(TaskAdvanceNoticeResourceAssembler.INSTANCE.parseList(this.baseService.searchTaskAdvanceNoticePageList(command)));
    }

    /**
     * 查询业务告警数据
     */
    @PostMapping("/selectAlarmBusinessList")
    @Operation(summary = "查询业务告警数据")
    public Result<List<BusinessAdvanceNoticeResource>> selectAlarmBusinessList(@RequestBody BusinessAdvanceQueryCommand command) {
        return Result.success(BusinessAdvanceNoticeResourceAssembler.INSTANCE.parseList(this.baseService.selectAlarmBusinessList(command)));
    }

    /**
     * 根据业务类型、业务id、告警类型查询告警信息
     */
    @Override
    @GetMapping("/searchTaskAdvanceNoticeList")
    public Result<List<TaskAdvanceNoticeResource>> searchTaskAdvanceNoticeList(@RequestParam("businessType") Integer businessType,
                                                                               @RequestParam("businessId") Serializable businessId,
                                                                               @RequestParam(value = "alarmType", required = false) Integer alarmType) {
        return Result.success(TaskAdvanceNoticeResourceAssembler.INSTANCE.parseList(this.baseService.searchTaskAdvanceNoticeList(businessType, businessId, alarmType)));
    }

    /**
     * 根据筛选条件检索计划变更预警分页列表数据
     */
    @Override
    @PostMapping("/searchPlanChangePageList")
    public Result<PagedResult<PlanChangeAdvanceNoticeResource>> searchPlanChangePageList(@RequestBody PlanItemAdvancePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        List<PlanChangeAdvanceNotice> entityList = this.baseService.searchPlanChangeAdvanceList(command.getCondition());
        return Result.success(entityList, PlanChangeAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 预警告警查询-计划变更预警-导出
     */
    @Operation(summary = "预警告警查询-计划变更预警-导出")
    @PostMapping("/searchPlanChangePageList/export")
    public void planChangeExport(@RequestBody PlanItemAdvancePageQueryCommand command, HttpServletResponse response) {
        baseService.planChangeExport(command, response);
    }

    /**
     * 根据筛选条件检索运方下达风险预警分页列表数据
     */
    @Override
    @PostMapping("/searchCarrierReleasePageList")
    public Result<PagedResult<CarrierReleaseAdvanceNoticeResource>> searchCarrierReleasePageList(@RequestBody TaskAdvancePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        List<CarrierReleaseAdvanceNotice> entityList = this.baseService.searchCarrierReleaseAdvanceList(command.getCondition());
        return Result.success(entityList, CarrierReleaseAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 预警告警查询-运方下达风险警告-导出
     */
    @Operation(summary = "预警告警查询-运方下达风险警告-导出")
    @PostMapping("/searchCarrierReleasePageList/export")
    public void carrierReleaseExport(@RequestBody TaskAdvancePageQueryCommand command, HttpServletResponse response) {
        baseService.carrierReleaseExport(command, response);
    }

    /**
     * 根据筛选条件检索下达超时预警分页列表数据
     */
    @Override
    @PostMapping("/searchReleaseTimeOutPageList")
    public Result<PagedResult<ReleaseTimeOutAdvanceNoticeResource>> searchReleaseTimeOutPageList(@RequestBody TaskUserAdvancePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        List<ReleaseTimeOutAdvanceNotice> entityList = this.baseService.searchReleaseTimeOutAdvanceList(command.getCondition());
        return Result.success(entityList, ReleaseTimeOutAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 预警告警查询-下达超时告警-导出
     */
    @Operation(summary = "预警告警查询-下达超时告警-导出")
    @PostMapping("/searchReleaseTimeOutPageList/export")
    public void releaseTimeOutExport(@RequestBody TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        baseService.releaseTimeOutExport(command, response);
    }

    /**
     * 根据筛选条件检索用户拒签预警分页列表数据
     */
    @Override
    @PostMapping("/searchUserRejectPageList")
    public Result<PagedResult<UserRejectAdvanceNoticeResource>> searchUserRejectPageList(@RequestBody TaskUserAdvancePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        List<UserRejectAdvanceNotice> entityList = this.baseService.searchUserRejectAdvanceList(command.getCondition());
        return Result.success(entityList, UserRejectAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 预警告警查询-用户拒签告警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    @Operation(summary = "预警告警查询-用户拒签告警-导出")
    @PostMapping("/searchUserRejectPageList/export")
    public void userRejectExport(@RequestBody TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        baseService.userRejectExport(command, response);
    }

    /**
     * 根据筛选条件检索重复停电预警分页列表数据
     */
    @Override
    @PostMapping("/searchRepeatPowerCutPageList")
    public Result<PagedResult<RepeatPowerCutAdvanceNoticeResource>> searchRepeatPowerCutPageList(@RequestBody TaskUserAdvancePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), command.getOrderBy());
        List<RepeatPowerCutAdvanceNotice> entityList = this.baseService.searchRepeatPowerCutAdvanceList(command.getCondition());
        return Result.success(entityList, RepeatPowerCutAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 预警告警查询-重复停电告警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    @Operation(summary = "预警告警查询-重复停电告警-导出")
    @PostMapping("/searchRepeatPowerCutPageList/export")
    public void repeatPowerCutExport(@RequestBody TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        baseService.repeatPowerCutExport(command, response);
    }

    /**
     * 保存计划变更预警(可批量)
     */
    @Override
    @PostMapping("/savePlanChangeAdvance")
    public Result<Map<String, Integer>> savePlanChangeAdvance(@RequestBody AdvanceNoticeSaveCommand command) {
        return Result.success(this.baseService.savePlanChangeAdvance(command));
    }

    /**
     * 保存运方下达风险预警(可批量)
     */
    @Override
    @PostMapping("/saveCarrierReleaseAdvance")
    public Result<Map<String, Integer>> saveCarrierReleaseAdvance(@RequestBody AdvanceNoticeSaveCommand command) {
        return Result.success(this.baseService.saveCarrierReleaseAdvance(command));
    }

    /**
     * 保存下达超时告警(可批量)
     */
    @Override
    @PostMapping("/saveReleaseTimeOutAdvance")
    public Result<Map<String, Integer>> saveReleaseTimeOutAdvance(@RequestBody AdvanceNoticeSaveCommand command) {
        return Result.success(this.baseService.saveReleaseTimeOutAdvance(command));
    }

    /**
     * 保存用户拒签告警(可批量)
     */
    @Override
    @PostMapping("/saveUserRejectAdvance")
    public Result<Map<String, Integer>> saveUserRejectAdvance(@RequestBody AdvanceNoticeSaveCommand command) {
        return Result.success(this.baseService.saveUserRejectAdvance(command));
    }

    /**
     * 保存重复停电告警(可批量)
     */
    @Override
    @PostMapping("/saveRepeatPowerCutAdvance")
    public Result<Map<String, Integer>> saveRepeatPowerCutAdvance(@RequestBody AdvanceNoticeSaveCommand command) {
        return Result.success(this.baseService.saveRepeatPowerCutAdvance(command));
    }

    /**
     * 修改告警状态
     */
    @PutMapping("/updateAdvanceNoticeStatus")
    @Operation(summary = "修改告警状态")
    public Result updateAdvanceNoticeStatus(@Valid @RequestBody AdvanceNoticeStatusUpdateCommand command) {
        this.baseService.updateAdvanceNoticeStatus(command);
        return Result.success();
    }

    /**
     * 取消计划变更告警状态
     */
    @Override
    @PutMapping("/cancelPlanChangeAdvanceStatus")
    public Result cancelPlanChangeAdvanceStatus(@RequestBody List<Serializable> planItemIdList) {
        this.baseService.cancelPlanChangeAdvanceStatus(planItemIdList);
        return Result.success();
    }

    /**
     * 取消运方下达风险预警状态
     */
    @Override
    @PutMapping("/cancelCarrierReleaseAdvanceStatus")
    public Result cancelCarrierReleaseAdvanceStatus(@RequestBody List<Serializable> taskIdList) {
        this.baseService.cancelCarrierReleaseAdvanceStatus(taskIdList);
        return Result.success();
    }

    /**
     * 取消下达超时告警状态
     */
    @Override
    @PutMapping("/cancelReleaseTimeOutAdvanceStatus")
    public Result cancelReleaseTimeOutAdvanceStatus(@RequestBody List<Serializable> taskUserIdList) {
        this.baseService.cancelReleaseTimeOutAdvanceStatus(taskUserIdList);
        return Result.success();
    }

    /**
     * 取消用户拒签告警状态
     */
    @Override
    @PutMapping("/cancelUserRejectAdvanceStatus")
    public Result cancelUserRejectAdvanceStatus(@RequestBody List<Serializable> taskUserIdList) {
        this.baseService.cancelUserRejectAdvanceStatus(taskUserIdList);
        return Result.success();
    }

    /**
     * 取消重复停电告警状态
     */
    @Override
    @PutMapping("/cancelRepeatPowerCutAdvanceStatus")
    public Result cancelRepeatPowerCutAdvanceStatus(@RequestBody List<Serializable> taskUserIdList) {
        this.baseService.cancelRepeatPowerCutAdvanceStatus(taskUserIdList);
        return Result.success();
    }

    /**
     * 预警数据统计
     */
    @PostMapping("/selectAlarmTotal")
    @Operation(summary = "预警数据统计")
    public Result<AdvanceNoticeTotalResource> selectAlarmTotal() {
        AdvanceNoticeTotalResource noticeTotalResource =  baseService.selectAlarmTotal();
        return Result.success(noticeTotalResource);
    }

    @Override
    protected TaskAdvanceNoticeResource toResource(TaskAdvanceNotice entity) {
        return TaskAdvanceNoticeResourceAssembler.INSTANCE.parse(entity);
    }

}