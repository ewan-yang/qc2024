package com.tecpie.platform.notice.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.notice.api.command.query.*;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.resource.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 预告警表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Tag(name = "预告警表接口定义")
@Headers("Accept: application/json")
public interface TaskAdvanceNoticeRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id
     * @return Result<TaskAdvanceNoticeResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/taskAdvanceNotice/{id}")
    Result<TaskAdvanceNoticeResource> getTaskAdvanceNotice(@Param("id") Serializable id);

    /**
     * 根据业务类型、业务id、告警类型查询告警信息
     *
     * @param businessType 业务类型
     * @param businessId   业务Id
     * @param alarmType    告警类型,如果为空，则查询当前业务所有预警
     * @return Result<List < TaskAdvanceNoticeResource>>
     */
    @Operation(summary = "根据业务类型、业务id、告警类型查询告警信息")
    @RequestLine("GET /api/v1/taskAdvanceNotice/searchTaskAdvanceNoticeList")
    Result<List<TaskAdvanceNoticeResource>> searchTaskAdvanceNoticeList(@Param("businessType") Integer businessType, @Param("businessId") Serializable businessId, @Param("alarmType") Integer alarmType);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command
     * @return Result<List < TaskAdvanceNoticeResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/list")
    Result<List<TaskAdvanceNoticeResource>> searchTaskAdvanceNoticeResourceList(TaskAdvanceNoticeQueryCommand command);

    /**
     * 计划变更预警分页列表数据
     *
     * @param command
     * @return Result<PagedResult < PlanChangeAdvanceNoticeResource>>
     */
    @Operation(summary = "计划变更预警分页列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/searchPlanChangePageList")
    Result<PagedResult<PlanChangeAdvanceNoticeResource>> searchPlanChangePageList(PlanItemAdvancePageQueryCommand command);

    /**
     * 运方下达风险预警分页列表数据
     *
     * @param command
     * @return Result<PagedResult < CarrierReleaseAdvanceNoticeResource>>
     */
    @Operation(summary = "运方下达风险预警分页列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/searchCarrierReleasePageList")
    Result<PagedResult<CarrierReleaseAdvanceNoticeResource>> searchCarrierReleasePageList(TaskAdvancePageQueryCommand command);

    /**
     * 下达超时预警分页列表数据
     *
     * @param command
     * @return Result<PagedResult < ReleaseTimeOutAdvanceNoticeResource>>
     */
    @Operation(summary = "下达超时预警分页列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/searchReleaseTimeOutPageList")
    Result<PagedResult<ReleaseTimeOutAdvanceNoticeResource>> searchReleaseTimeOutPageList(TaskUserAdvancePageQueryCommand command);

    /**
     * 用户拒签预警分页列表数据
     *
     * @param command
     * @return Result<PagedResult < UserRejectAdvanceNoticeResource>>
     */
    @Operation(summary = "用户拒签预警分页列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/searchUserRejectPageList")
    Result<PagedResult<UserRejectAdvanceNoticeResource>> searchUserRejectPageList(TaskUserAdvancePageQueryCommand command);

    /**
     * 重复停电预警分页列表数据
     *
     * @param command
     * @return Result<PagedResult < RepeatPowerCutAdvanceNoticeResource>>
     */
    @Operation(summary = "重复停电预警分页列表数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/searchRepeatPowerCutPageList")
    Result<PagedResult<RepeatPowerCutAdvanceNoticeResource>> searchRepeatPowerCutPageList(TaskUserAdvancePageQueryCommand command);

    /**
     * 保存计划变更预警(可批量)
     *
     * @param command 数据对象
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "保存计划变更预警(可批量)")
    @RequestLine("POST /api/v1/taskAdvanceNotice/savePlanChangeAdvance")
    Result<Map<String, Integer>> savePlanChangeAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存运方下达风险预警(可批量)
     *
     * @param command 数据对象
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "保存运方下达风险预警(可批量)")
    @RequestLine("POST /api/v1/taskAdvanceNotice/saveCarrierReleaseAdvance")
    Result<Map<String, Integer>> saveCarrierReleaseAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存下达超时告警(可批量)
     *
     * @param command 数据对象
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "保存下达超时告警(可批量)")
    @RequestLine("POST /api/v1/taskAdvanceNotice/saveReleaseTimeOutAdvance")
    Result<Map<String, Integer>> saveReleaseTimeOutAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存用户拒签告警(可批量)
     *
     * @param command 数据对象
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "保存用户拒签告警(可批量)")
    @RequestLine("POST /api/v1/taskAdvanceNotice/saveUserRejectAdvance")
    Result<Map<String, Integer>> saveUserRejectAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存重复停电告警(可批量)
     *
     * @param command 数据对象
     * @return Result<Map < String, Integer>>
     */
    @Operation(summary = "保存重复停电告警(可批量)")
    @RequestLine("POST /api/v1/taskAdvanceNotice/saveRepeatPowerCutAdvance")
    Result<Map<String, Integer>> saveRepeatPowerCutAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 取消计划变更告警状态
     */
    @Operation(summary = "取消计划变更告警状态")
    @RequestLine("PUT /api/v1/taskAdvanceNotice/cancelPlanChangeAdvanceStatus")
    Result cancelPlanChangeAdvanceStatus(List<Serializable> planItemIdList);

    /**
     * 取消运方下达风险预警状态
     */
    @Operation(summary = "取消运方下达风险预警状态")
    @RequestLine("PUT /api/v1/taskAdvanceNotice/cancelCarrierReleaseAdvanceStatus")
    Result cancelCarrierReleaseAdvanceStatus(List<Serializable> taskIdList);

    /**
     * 取消下达超时告警状态
     */
    @Operation(summary = "取消下达超时告警状态")
    @RequestLine("PUT /api/v1/taskAdvanceNotice/cancelReleaseTimeOutAdvanceStatus")
    Result cancelReleaseTimeOutAdvanceStatus(List<Serializable> taskUserIdList);

    /**
     * 取消用户拒签告警状态
     */
    @Operation(summary = "取消用户拒签告警状态")
    @RequestLine("PUT /api/v1/taskAdvanceNotice/cancelUserRejectAdvanceStatus")
    Result cancelUserRejectAdvanceStatus(List<Serializable> taskUserIdList);

    /**
     * 取消重复停电告警状态
     */
    @Operation(summary = "取消重复停电告警状态")
    @RequestLine("PUT /api/v1/taskAdvanceNotice/cancelRepeatPowerCutAdvanceStatus")
    Result cancelRepeatPowerCutAdvanceStatus(List<Serializable> taskUserIdList);

    @Operation(summary = "预警数据统计")
    @RequestLine("POST /api/v1/taskAdvanceNotice/selectAlarmTotal")
    Result<AdvanceNoticeTotalResource> selectAlarmTotal();

    @Operation(summary = "查询业务告警数据")
    @RequestLine("POST /api/v1/taskAdvanceNotice/selectAlarmBusinessList")
    Result<List<BusinessAdvanceNoticeResource>> selectAlarmBusinessList(BusinessAdvanceQueryCommand command);

}