package com.tecpie.platform.notice.service;

import com.tecpie.platform.notice.api.command.query.*;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.update.AdvanceNoticeStatusUpdateCommand;
import com.tecpie.platform.notice.api.resource.AdvanceNoticeTotalResource;
import com.tecpie.platform.notice.entity.*;
import com.tecpie.platform.notice.mapper.TaskAdvanceNoticeMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 预告警表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
public interface TaskAdvanceNoticeService extends IBaseService<TaskAdvanceNoticeMapper, TaskAdvanceNotice> {

    /**
     * 获取详情信息
     *
     * @param id 主键ID
     * @return TaskAdvanceNotice
     */
    TaskAdvanceNotice selectExtensionById(Serializable id);

    /**
     * 检索预警列表
     *
     * @param command 查询参数
     * @return List<TaskAdvanceNotice>
     */
    List<TaskAdvanceNotice> searchTaskAdvanceNoticePageList(TaskAdvanceNoticeQueryCommand command);

    /**
     * 查询业务告警数据
     *
     * @param command 查询参数
     * @return List<BusinessAdvanceNotice>
     */
    List<BusinessAdvanceNotice> selectAlarmBusinessList(BusinessAdvanceQueryCommand command);

    /**
     * 根据业务类型、业务id、告警类型查询告警信息
     *
     * @param businessType 业务类型
     * @param businessId   业务Id
     * @param alarmType    告警类型,如果为空，则查询当前业务所有预警
     * @return List<TaskAdvanceNotice>
     */
    List<TaskAdvanceNotice> searchTaskAdvanceNoticeList(Integer businessType, Serializable businessId, Integer alarmType);

    /**
     * 根据条件查询告警信息
     *
     * @param businessType   业务类型
     * @param businessIdList 业务IdList
     * @param alarmType      告警类型,如果为空，则查询当前业务所有预警
     * @return List<TaskAdvanceNotice>
     */
    List<TaskAdvanceNotice> searchTaskAdvanceNoticeList(Integer businessType, List<Serializable> businessIdList, Integer alarmType);

    /**
     * 计划变更告警数据
     *
     * @param command 查询参数
     * @return List<PlanChangeAdvanceNotice>
     */
    List<PlanChangeAdvanceNotice> searchPlanChangeAdvanceList(PlanItemAdvanceQueryCommand command);

    /**
     * 运方下达风险预警数据
     *
     * @param command 查询参数
     * @return List<CarrierReleaseAdvanceNotice>
     */
    List<CarrierReleaseAdvanceNotice> searchCarrierReleaseAdvanceList(TaskAdvanceQueryCommand command);

    /**
     * 用户下达超时预警数据
     *
     * @param command 查询参数
     * @return List<ReleaseTimeOutAdvanceNotice>
     */
    List<ReleaseTimeOutAdvanceNotice> searchReleaseTimeOutAdvanceList(TaskUserAdvanceQueryCommand command);

    /**
     * 用户拒签预警数据
     *
     * @param command 查询参数
     * @return List<UserRejectAdvanceNotice>
     */
    List<UserRejectAdvanceNotice> searchUserRejectAdvanceList(TaskUserAdvanceQueryCommand command);

    /**
     * 用户重复停电预警
     *
     * @param command 查询参数
     * @return List<RepeatPowerCutAdvanceNotice>
     */
    List<RepeatPowerCutAdvanceNotice> searchRepeatPowerCutAdvanceList(TaskUserAdvanceQueryCommand command);

    /**
     * 保存计划变更预警(可批量)
     *
     * @param command 数据对象
     * @return Map<String, Integer>
     */
    Map<String, Integer> savePlanChangeAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存运方下达风险预警(可批量)
     *
     * @param command 数据对象
     * @return Map<String, Integer>
     */
    Map<String, Integer> saveCarrierReleaseAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存下达超时告警(可批量)
     *
     * @param command 数据对象
     * @return Map<String, Integer>
     */
    Map<String, Integer> saveReleaseTimeOutAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存用户拒签告警(可批量)
     *
     * @param command 数据对象
     * @return Map<String, Integer>
     */
    Map<String, Integer> saveUserRejectAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 保存重复停电告警(可批量)
     *
     * @param command 数据对象
     * @return Map<String, Integer>
     */
    Map<String, Integer> saveRepeatPowerCutAdvance(AdvanceNoticeSaveCommand command);

    /**
     * 修改告警状态
     *
     * @param command 数据对象
     */
    void updateAdvanceNoticeStatus(AdvanceNoticeStatusUpdateCommand command);

    /**
     * 取消计划变更告警状态
     *
     * @param planItemIdList 计划项IdList
     */
    void cancelPlanChangeAdvanceStatus(List<Serializable> planItemIdList);

    /**
     * 取消运方下达风险预警状态
     *
     * @param taskIdList 停电任务通知IdList
     */
    void cancelCarrierReleaseAdvanceStatus(List<Serializable> taskIdList);

    /**
     * 取消下达超时告警状态
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    void cancelReleaseTimeOutAdvanceStatus(List<Serializable> taskUserIdList);

    /**
     * 取消用户拒签告警状态
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    void cancelUserRejectAdvanceStatus(List<Serializable> taskUserIdList);

    /**
     * 取消重复停电告警状态
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    void cancelRepeatPowerCutAdvanceStatus(List<Serializable> taskUserIdList);

    /**
     * 预警数据统计
     *
     * @param alarmType 告警类型
     * @return int
     */
    int selectAlarmTotal(Integer alarmType);

    /**
     * 预警数据统计
     * 实时
     */
    AdvanceNoticeTotalResource selectAlarmTotal();

    /**
     * 预警告警查询-运方下达风险警告-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void carrierReleaseExport(TaskAdvancePageQueryCommand command, HttpServletResponse response);

    /**
     * 预警告警查询-下达超时告警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void releaseTimeOutExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response);

    /**
     * 预警告警查询-计划变更预警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void planChangeExport(PlanItemAdvancePageQueryCommand command, HttpServletResponse response);

    /**
     * 预警告警查询-用户拒签告警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void userRejectExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response);

    /**
     * 预警告警查询-重复停电告警-导出
     *
     * @param command  命令
     * @param response 响应
     */
    void repeatPowerCutExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response);
}