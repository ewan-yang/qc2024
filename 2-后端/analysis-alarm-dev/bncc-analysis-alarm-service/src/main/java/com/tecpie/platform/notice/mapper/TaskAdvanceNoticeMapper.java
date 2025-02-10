package com.tecpie.platform.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.notice.api.command.query.*;
import com.tecpie.platform.notice.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电计划/任务预告警表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Repository
public interface TaskAdvanceNoticeMapper extends BaseMapper<TaskAdvanceNotice> {

    /**
     * 获取详情信息
     *
     * @param id 主键ID
     * @return TaskAdvanceNotice
     */
    TaskAdvanceNotice selectExtensionById(@Param("id") Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskAdvanceNotice>
     */
    List<TaskAdvanceNotice> searchExtensionPageList(@Param("condition") TaskAdvanceNoticeQueryCommand command);

    /**
     * 查询业务告警数据
     *
     * @param command 查询参数
     * @return List<BusinessAdvanceNotice>
     */
    List<BusinessAdvanceNotice> selectAlarmBusinessList(@Param("condition") BusinessAdvanceQueryCommand command);

    /**
     * 计划变更告警数据
     *
     * @param command 查询参数
     * @return List<PlanChangeAdvanceNotice>
     */
    List<PlanChangeAdvanceNotice> searchPlanChangeAdvanceList(@Param("condition") PlanItemAdvanceQueryCommand command);

    /**
     * 运方下达风险预警数据
     *
     * @param command 查询参数
     * @return List<CarrierReleaseAdvanceNotice>
     */
    List<CarrierReleaseAdvanceNotice> searchCarrierReleaseAdvanceList(@Param("condition") TaskAdvanceQueryCommand command);

    /**
     * 用户下达超时预警数据
     *
     * @param command 查询参数
     * @return List<ReleaseTimeOutAdvanceNotice>
     */
    List<ReleaseTimeOutAdvanceNotice> searchReleaseTimeOutAdvanceList(@Param("condition") TaskUserAdvanceQueryCommand command);

    /**
     * 用户拒签预警数据
     *
     * @param command 查询参数
     * @return List<UserRejectAdvanceNotice>
     */
    List<UserRejectAdvanceNotice> searchUserRejectAdvanceList(@Param("condition") TaskUserAdvanceQueryCommand command);

    /**
     * 用户重复停电预警
     *
     * @param command 查询参数
     * @return List<RepeatPowerCutAdvanceNotice>
     */
    List<RepeatPowerCutAdvanceNotice> searchRepeatPowerCutAdvanceList(@Param("condition") TaskUserAdvanceQueryCommand command);

    /**
     * 预警数据统计
     *
     * @param alarmType 告警类型
     * @param alarmTime 告警时间
     * @return int
     */
    int selectAlarmTotal(@Param("alarmType") Integer alarmType, @Param("alarmTime") LocalDateTime alarmTime);

}