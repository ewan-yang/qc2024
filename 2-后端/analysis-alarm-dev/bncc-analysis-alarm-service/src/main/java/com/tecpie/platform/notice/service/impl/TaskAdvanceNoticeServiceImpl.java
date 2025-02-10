package com.tecpie.platform.notice.service.impl;

import cn.hutool.core.date.DatePattern;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.enums.AlarmTypeEnum;
import com.tecpie.platform.common.enums.BusinessTypeEnum;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.notice.api.command.query.*;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.save.AdvanceParamCommand;
import com.tecpie.platform.notice.api.command.update.AdvanceNoticeStatusUpdateCommand;
import com.tecpie.platform.notice.api.resource.*;
import com.tecpie.platform.notice.controller.assembler.resource.*;
import com.tecpie.platform.notice.entity.*;
import com.tecpie.platform.notice.mapper.TaskAdvanceNoticeMapper;
import com.tecpie.platform.notice.service.TaskAdvanceNoticeService;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.service.TaskAdvanceRuleService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预告警表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskAdvanceNoticeServiceImpl extends BaseServiceImpl<TaskAdvanceNoticeMapper, TaskAdvanceNotice> implements TaskAdvanceNoticeService {

    private TaskAdvanceRuleService taskAdvanceRuleService;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setTaskAdvanceNoticeServiceImpl(TaskAdvanceRuleService taskAdvanceRuleService, ExcelTransfer excelTransfer) {
        this.taskAdvanceRuleService = taskAdvanceRuleService;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public TaskAdvanceNotice selectExtensionById(Serializable id) {
        TaskAdvanceNotice entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("预告警表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<TaskAdvanceNotice> searchTaskAdvanceNoticePageList(TaskAdvanceNoticeQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<BusinessAdvanceNotice> selectAlarmBusinessList(BusinessAdvanceQueryCommand command) {
        command.initDateParam();
        List<BusinessAdvanceNotice> advanceNoticeList = this.baseMapper.selectAlarmBusinessList(command);
        if (!CollectionUtils.isEmpty(advanceNoticeList)) {
            advanceNoticeList.forEach(item -> {
                // 停电时间
                String startTime = item.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                // 送电时间
                String endTime = item.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                // 告警类型
                Integer alarmType = item.getAlarmType();
                if (AlarmTypeEnum.JH_BG.getCode().equals(alarmType)) {
                    item.setAlarmContent(String.format("[%s]停电计划，计划于[%s]到[%s]对该地区进行停电，请及时处理！", item.getBusinessCode(), startTime, endTime));
                } else if (AlarmTypeEnum.XD_FX.getCode().equals(alarmType)) {
                    item.setAlarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前还未全部下达，已超时！", item.getBusinessCode(), startTime, endTime));
                } else if (AlarmTypeEnum.XD_CH.getCode().equals(alarmType)) {
                    item.setAlarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前用户[%s]还未下达，已超时！", item.getBusinessContent(), startTime, endTime, item.getBusinessCode()));
                } else if (AlarmTypeEnum.YH_JQ.getCode().equals(alarmType)) {
                    item.setAlarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前用户[%s]拒绝签字！", item.getBusinessContent(), startTime, endTime, item.getBusinessCode()));
                } else if (AlarmTypeEnum.CH_TD.getCode().equals(alarmType)) {
                    item.setAlarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，用户[%s]属于重复停电！", item.getBusinessContent(), startTime, endTime, item.getBusinessCode()));
                }
            });
        }
        return advanceNoticeList;
    }

    @Override
    public List<TaskAdvanceNotice> searchTaskAdvanceNoticeList(Integer businessType, Serializable businessId, Integer alarmType) {
        return this.searchTaskAdvanceNoticeList(businessType, Collections.singletonList(businessId), alarmType);
    }

    @Override
    public List<TaskAdvanceNotice> searchTaskAdvanceNoticeList(Integer businessType, List<Serializable> businessIdList, Integer alarmType) {
        TaskAdvanceNoticeQueryCommand command = new TaskAdvanceNoticeQueryCommand();
        command.setBusinessType(businessType);
        command.setBusinessIdList(businessIdList);
        command.setAlarmType(alarmType);
        return this.searchTaskAdvanceNoticePageList(command);
    }

    @Override
    public List<PlanChangeAdvanceNotice> searchPlanChangeAdvanceList(PlanItemAdvanceQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchPlanChangeAdvanceList(command);
    }

    @Override
    public List<CarrierReleaseAdvanceNotice> searchCarrierReleaseAdvanceList(TaskAdvanceQueryCommand command) {
        command.initDateParam();
        List<CarrierReleaseAdvanceNotice> releaseAdvanceNoticeList = this.baseMapper.searchCarrierReleaseAdvanceList(command);
        if (!CollectionUtils.isEmpty(releaseAdvanceNoticeList)) {
            // 获取下达分险告警规则天数
            TaskAdvanceRule carrierReleaseRule = taskAdvanceRuleService.getCarrierReleaseRule();
            if (ObjectUtils.isNotEmpty(carrierReleaseRule)) {
                // 计算超期时长
                releaseAdvanceNoticeList.forEach(e -> e.calOverdueDuration(carrierReleaseRule.getDays()));
            }
        }
        return releaseAdvanceNoticeList;
    }

    @Override
    public List<ReleaseTimeOutAdvanceNotice> searchReleaseTimeOutAdvanceList(TaskUserAdvanceQueryCommand command) {
        command.initDateParam();
        List<ReleaseTimeOutAdvanceNotice> releaseTimeOutAdvanceNoticeList = this.baseMapper.searchReleaseTimeOutAdvanceList(command);
        if (!CollectionUtils.isEmpty(releaseTimeOutAdvanceNoticeList)) {
            // 计算距离停电时长
            releaseTimeOutAdvanceNoticeList.forEach(ReleaseTimeOutAdvanceNotice::catDistanceDuration);
        }
        return releaseTimeOutAdvanceNoticeList;
    }

    @Override
    public List<UserRejectAdvanceNotice> searchUserRejectAdvanceList(TaskUserAdvanceQueryCommand command) {
        command.initDateParam();
        List<UserRejectAdvanceNotice> userRejectAdvanceNoticeList = this.baseMapper.searchUserRejectAdvanceList(command);
        if (!CollectionUtils.isEmpty(userRejectAdvanceNoticeList)) {
            // 获取反馈超期告警规则天数
            TaskAdvanceRule feedbackTimeOutRule = taskAdvanceRuleService.getFeedbackTimeOutRule();
            if (ObjectUtils.isNotEmpty(feedbackTimeOutRule)) {
                // 计算反馈超期时长
                userRejectAdvanceNoticeList.forEach(e -> e.calFeedbackOverdueDuration(feedbackTimeOutRule.getDays()));
            }
        }
        return userRejectAdvanceNoticeList;
    }

    @Override
    public List<RepeatPowerCutAdvanceNotice> searchRepeatPowerCutAdvanceList(TaskUserAdvanceQueryCommand command) {
        command.initDateParam();
        List<RepeatPowerCutAdvanceNotice> repeatPowerCutAdvanceNoticeList = this.baseMapper.searchRepeatPowerCutAdvanceList(command);
        if (!CollectionUtils.isEmpty(repeatPowerCutAdvanceNoticeList)) {
            // 停电间隔天数
            repeatPowerCutAdvanceNoticeList.forEach(RepeatPowerCutAdvanceNotice::catIntervalDays);
        }
        return repeatPowerCutAdvanceNoticeList;
    }

    /**
     * 获取已经存在的业务数据id
     */
    private List<String> getBusinessIdList(Integer businessType, List<Serializable> businessIdList, Integer alarmType) {
        List<String> businessIdDbList = Lists.newArrayList();
        // 先查询业务告警数据
        List<TaskAdvanceNotice> advanceNoticeList = this.searchTaskAdvanceNoticeList(businessType, businessIdList, alarmType);
        if (!CollectionUtils.isEmpty(advanceNoticeList)) {
            businessIdDbList = advanceNoticeList.stream().map(e -> e.getBusinessId().toString()).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(businessIdDbList)) {
            businessIdDbList = Lists.newArrayList();
        }
        return businessIdDbList;
    }

    @Override
    public Map<String, Integer> savePlanChangeAdvance(AdvanceNoticeSaveCommand command) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            List<AdvanceParamCommand> businessList = command.getBusinessList();
            // 先查询业务告警数据
            List<String> businessIdList = this.getBusinessIdList(BusinessTypeEnum.PLAN_MODEL.getCode(), command.getBusinessIdList(), AlarmTypeEnum.JH_BG.getCode());
            List<TaskAdvanceNotice> addList = Lists.newArrayList();
            for (AdvanceParamCommand paramCommand : businessList) {
                String businessId = paramCommand.getBusinessId().toString();
                // 作为返回项，每个业务ID对应的告警类型
                resultMap.put(businessId, AlarmTypeEnum.JH_BG.getCode());
                // 重复判断
                if (businessIdList.contains(businessId)) {
                    log.info(String.format("当前计划项ID[%s]计划变更预警数据已存在，不需要重复添加", businessId));
                    continue;
                }
                // 停电时间
                String startTime = paramCommand.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                // 送电时间
                String endTime = paramCommand.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                addList.add(TaskAdvanceNotice.builder()
                        .businessType(BusinessTypeEnum.PLAN_MODEL.getCode())
                        .businessId(paramCommand.getBusinessId())
                        .alarmType(AlarmTypeEnum.JH_BG.getCode())
                        .alarmTitle("计划变更预警")
                        .alarmContent(String.format("[%s]停电计划，计划于[%s]到[%s]对该地区进行停电，请及时处理！", paramCommand.getBusinessCode(), startTime, endTime))
                        .alarmTime(LocalDateTime.now())
                        .build());
            }
            // 保存停电计划告警
            if (!CollectionUtils.isEmpty(addList)) {
                this.saveBatch(addList);
            }
        } catch (Exception e) {
            log.error("保存停电计划告警失败", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> saveCarrierReleaseAdvance(AdvanceNoticeSaveCommand command) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            List<AdvanceParamCommand> businessList = command.getBusinessList();
            // 先查询业务告警数据
            List<String> businessIdList = this.getBusinessIdList(BusinessTypeEnum.TASK_MODEL.getCode(), command.getBusinessIdList(), AlarmTypeEnum.XD_FX.getCode());
            List<TaskAdvanceNotice> addList = Lists.newArrayList();
            for (AdvanceParamCommand paramCommand : businessList) {
                String businessId = paramCommand.getBusinessId().toString();
                // 作为返回项，每个业务ID对应的告警类型
                resultMap.put(businessId, AlarmTypeEnum.XD_FX.getCode());
                // 重复判断
                if (businessIdList.contains(businessId)) {
                    log.info(String.format("当前任务ID[%s]下达风险预警数据已存在，不需要重复添加", businessId));
                    continue;
                }
                // 停电时间
                String startTime = paramCommand.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 送电时间
                String endTime = paramCommand.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                addList.add(TaskAdvanceNotice.builder()
                        .businessType(BusinessTypeEnum.TASK_MODEL.getCode())
                        .businessId(paramCommand.getBusinessId())
                        .alarmType(AlarmTypeEnum.XD_FX.getCode())
                        .alarmTitle("下达风险预警")
                        .alarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前还未全部下达，已超时！", paramCommand.getTaskCode(), startTime, endTime))
                        .alarmTime(LocalDateTime.now())
                        .attribute1("告警规则维护的天数：" + command.getDays())
                        .build());
            }
            // 保存运方下达风险告警
            if (!CollectionUtils.isEmpty(addList)) {
                this.saveBatch(addList);
            }
        } catch (Exception e) {
            log.error("保存运方下达风险告警失败", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> saveReleaseTimeOutAdvance(AdvanceNoticeSaveCommand command) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            List<AdvanceParamCommand> businessList = command.getBusinessList();
            // 先查询业务告警数据
            List<String> businessIdList = this.getBusinessIdList(BusinessTypeEnum.TASK_USER_MODEL.getCode(), command.getBusinessIdList(), AlarmTypeEnum.XD_CH.getCode());
            List<TaskAdvanceNotice> addList = Lists.newArrayList();
            for (AdvanceParamCommand paramCommand : businessList) {
                String businessId = paramCommand.getBusinessId().toString();
                // 作为返回项，每个业务ID对应的告警类型
                resultMap.put(businessId, AlarmTypeEnum.XD_CH.getCode());
                // 重复判断
                if (businessIdList.contains(businessId)) {
                    log.info(String.format("当前用户ID[%s]下达超时预警数据已存在，不需要重复添加", businessId));
                    continue;
                }
                // 停电时间
                String startTime = paramCommand.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 送电时间
                String endTime = paramCommand.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                addList.add(TaskAdvanceNotice.builder()
                        .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode())
                        .businessId(paramCommand.getBusinessId())
                        .alarmType(AlarmTypeEnum.XD_CH.getCode())
                        .alarmTitle("下达超时预警")
                        .alarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前用户[%s]还未下达，已超时！", paramCommand.getTaskCode(), startTime, endTime, paramCommand.getBusinessCode()))
                        .alarmTime(LocalDateTime.now())
                        .attribute1("告警规则维护的天数：" + command.getDays())
                        .build());
            }
            // 保存停电用户下达超时告警
            if (!CollectionUtils.isEmpty(addList)) {
                this.saveBatch(addList);
            }
        } catch (Exception e) {
            log.error("保存停电用户下达超时告警失败", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> saveUserRejectAdvance(AdvanceNoticeSaveCommand command) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            List<AdvanceParamCommand> businessList = command.getBusinessList();
            // 先查询业务告警数据
            List<String> businessIdList = this.getBusinessIdList(BusinessTypeEnum.TASK_USER_MODEL.getCode(), command.getBusinessIdList(), AlarmTypeEnum.YH_JQ.getCode());
            List<TaskAdvanceNotice> addList = Lists.newArrayList();
            for (AdvanceParamCommand paramCommand : businessList) {
                String businessId = paramCommand.getBusinessId().toString();
                // 作为返回项，每个业务ID对应的告警类型
                resultMap.put(businessId, AlarmTypeEnum.YH_JQ.getCode());
                // 重复判断
                if (businessIdList.contains(businessId)) {
                    log.info(String.format("当前用户ID[%s]用户拒签预警数据已存在，不需要重复添加", businessId));
                    continue;
                }
                // 停电时间
                String startTime = paramCommand.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 送电时间
                String endTime = paramCommand.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                addList.add(TaskAdvanceNotice.builder()
                        .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode())
                        .businessId(paramCommand.getBusinessId())
                        .alarmType(AlarmTypeEnum.YH_JQ.getCode())
                        .alarmTitle("用户拒签预警")
                        .alarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，目前用户[%s]拒绝签字！", paramCommand.getTaskCode(), startTime, endTime, paramCommand.getBusinessCode()))
                        .alarmTime(LocalDateTime.now())
                        .attribute1("告警规则维护的天数：" + command.getDays())
                        // 最近一次反馈时间
                        .attribute2(paramCommand.getLastTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                        .build());
            }
            // 保存停电用户拒签告警
            if (!CollectionUtils.isEmpty(addList)) {
                this.saveBatch(addList);
            }
        } catch (Exception e) {
            log.error("保存停电用户拒签告警失败", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> saveRepeatPowerCutAdvance(AdvanceNoticeSaveCommand command) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            List<AdvanceParamCommand> businessList = command.getBusinessList();
            // 先查询业务告警数据
            List<String> businessIdList = this.getBusinessIdList(BusinessTypeEnum.TASK_USER_MODEL.getCode(), command.getBusinessIdList(), AlarmTypeEnum.CH_TD.getCode());
            List<TaskAdvanceNotice> addList = Lists.newArrayList();
            for (AdvanceParamCommand paramCommand : businessList) {
                String businessId = paramCommand.getBusinessId().toString();
                // 作为返回项，每个业务ID对应的告警类型
                resultMap.put(businessId, AlarmTypeEnum.CH_TD.getCode());
                // 重复判断
                if (businessIdList.contains(businessId)) {
                    log.info(String.format("当前用户ID[%s]重复停电预警数据已存在，不需要重复添加", businessId));
                    continue;
                }
                // 停电时间
                String startTime = paramCommand.getStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 送电时间
                String endTime = paramCommand.getEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
                // 告警数据
                addList.add(TaskAdvanceNotice.builder()
                        .businessType(BusinessTypeEnum.TASK_USER_MODEL.getCode())
                        .businessId(paramCommand.getBusinessId())
                        .alarmType(AlarmTypeEnum.CH_TD.getCode())
                        .alarmTitle("重复停电预警")
                        .alarmContent(String.format("[%s]停电任务，计划于[%s]到[%s]对该地区进行停电，用户[%s]属于重复停电！", paramCommand.getTaskCode(), startTime, endTime, paramCommand.getBusinessCode()))
                        .alarmTime(LocalDateTime.now())
                        .attribute1("告警规则维护的天数：" + paramCommand.getDays())
                        // 最近一次停电时间
                        .attribute2(paramCommand.getLastTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN)))
                        .build());
            }
            // 保存用户重复停电告警
            if (!CollectionUtils.isEmpty(addList)) {
                this.saveBatch(addList);
            }
        } catch (Exception e) {
            log.error("保存用户重复停电告警失败", e);
        }
        return resultMap;
    }

    @Override
    public void updateAdvanceNoticeStatus(AdvanceNoticeStatusUpdateCommand command) {
        List<TaskAdvanceNotice> advanceNoticeList = this.searchTaskAdvanceNoticeList(command.getBusinessType(), command.getBusinessIdList(), command.getAlarmType());
        if (!CollectionUtils.isEmpty(advanceNoticeList)) {
            // 更新状态
            this.lambdaUpdate().set(TaskAdvanceNotice::getStatus, command.getStatus())
                    .in(TaskAdvanceNotice::getId, advanceNoticeList.stream().map(TaskAdvanceNotice::getId).collect(Collectors.toList()));
        }
    }

    private void cancelAdvanceStatus(Integer businessType, List<Serializable> businessIdList, Integer alarmType) {
        try {
            List<TaskAdvanceNotice> advanceNoticeList = this.searchTaskAdvanceNoticeList(businessType, businessIdList, alarmType);
            if (!CollectionUtils.isEmpty(advanceNoticeList)) {
                // 更新状态
                this.lambdaUpdate().set(TaskAdvanceNotice::getStatus, AlarmTypeEnum.ZC.getCode())
                        .in(TaskAdvanceNotice::getId, advanceNoticeList.stream().map(TaskAdvanceNotice::getId).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.error("取消告警状态更新失败", e);
        }
    }

    @Override
    public void cancelPlanChangeAdvanceStatus(List<Serializable> planItemIdList) {
        this.cancelAdvanceStatus(BusinessTypeEnum.PLAN_MODEL.getCode(), planItemIdList, AlarmTypeEnum.JH_BG.getCode());
    }

    @Override
    public void cancelCarrierReleaseAdvanceStatus(List<Serializable> taskIdList) {
        this.cancelAdvanceStatus(BusinessTypeEnum.TASK_MODEL.getCode(), taskIdList, AlarmTypeEnum.XD_FX.getCode());
    }

    @Override
    public void cancelReleaseTimeOutAdvanceStatus(List<Serializable> taskUserIdList) {
        this.cancelAdvanceStatus(BusinessTypeEnum.TASK_USER_MODEL.getCode(), taskUserIdList, AlarmTypeEnum.XD_CH.getCode());
    }

    @Override
    public void cancelUserRejectAdvanceStatus(List<Serializable> taskUserIdList) {
        this.cancelAdvanceStatus(BusinessTypeEnum.TASK_USER_MODEL.getCode(), taskUserIdList, AlarmTypeEnum.YH_JQ.getCode());
    }

    @Override
    public void cancelRepeatPowerCutAdvanceStatus(List<Serializable> taskUserIdList) {
        this.cancelAdvanceStatus(BusinessTypeEnum.TASK_USER_MODEL.getCode(), taskUserIdList, AlarmTypeEnum.CH_TD.getCode());
    }

    @Override
    public int selectAlarmTotal(Integer alarmType) {
        LocalDateTime alarmTime = LocalDateTime.now().minusDays(30);
        return this.baseMapper.selectAlarmTotal(alarmType, alarmTime);
    }

    @Override
    public AdvanceNoticeTotalResource selectAlarmTotal() {
        // 构建返回结果
        AdvanceNoticeTotalResource resource = new AdvanceNoticeTotalResource();
        List<BusinessAdvanceNotice> list = this.selectAlarmBusinessList(new BusinessAdvanceQueryCommand());
        list.forEach(t -> {
            Integer businessType = t.getBusinessType() == null ? 999 : t.getBusinessType();
            Integer alarmType = t.getAlarmType() == null ? 999 : t.getAlarmType();
            // 2+3=用户拒签告警
            if (businessType.equals(2) && alarmType.equals(3)) {
                resource.setUserRejectCount(resource.getUserRejectCount() + 1);
            } else if (businessType.equals(2) && alarmType.equals(1)) {
                // 2+1=下达超时
                resource.setReleaseTimeOutCount(resource.getReleaseTimeOutCount() + 1);
            } else if (businessType.equals(2) && alarmType.equals(4)) {
                // 2+4=重复停电
                resource.setRepeatPowerCutCount(resource.getRepeatPowerCutCount() + 1);
            } else if (businessType.equals(3) && alarmType.equals(5)) {
                resource.setPlanItemChangeCount(resource.getPlanItemChangeCount() + 1);
            } else if (businessType.equals(1) && alarmType.equals(2)) {
                resource.setCarrierReleaseCount(resource.getCarrierReleaseCount() + 1);
            }
        });
        return resource;
    }

    @Override
    public void carrierReleaseExport(TaskAdvancePageQueryCommand command, HttpServletResponse response) {
        List<CarrierReleaseAdvanceNotice> entityList = this.searchCarrierReleaseAdvanceList(command.getCondition());
        List<CarrierReleaseAdvanceNoticeResource> list = CarrierReleaseAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "运放下达风险预警", "sheet1", CarrierReleaseAdvanceNoticeResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseTimeOutExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        List<ReleaseTimeOutAdvanceNotice> entityList = this.searchReleaseTimeOutAdvanceList(command.getCondition());
        List<ReleaseTimeOutAdvanceNoticeResource> list = ReleaseTimeOutAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "下达超时告警", "sheet1", ReleaseTimeOutAdvanceNoticeResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void planChangeExport(PlanItemAdvancePageQueryCommand command, HttpServletResponse response) {
        List<PlanChangeAdvanceNotice> entityList = this.searchPlanChangeAdvanceList(command.getCondition());
        List<PlanChangeAdvanceNoticeResource> list = PlanChangeAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "计划变更预警", "sheet1", PlanChangeAdvanceNoticeResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void userRejectExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        List<UserRejectAdvanceNotice> entityList = this.searchUserRejectAdvanceList(command.getCondition());
        List<UserRejectAdvanceNoticeResource> list = UserRejectAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "用户拒签告警", "sheet1", UserRejectAdvanceNoticeResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void repeatPowerCutExport(TaskUserAdvancePageQueryCommand command, HttpServletResponse response) {
        List<RepeatPowerCutAdvanceNotice> entityList = this.searchRepeatPowerCutAdvanceList(command.getCondition());
        List<RepeatPowerCutAdvanceNoticeResource> list = RepeatPowerCutAdvanceNoticeResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "重复停电告警", "sheet1", RepeatPowerCutAdvanceNoticeResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}