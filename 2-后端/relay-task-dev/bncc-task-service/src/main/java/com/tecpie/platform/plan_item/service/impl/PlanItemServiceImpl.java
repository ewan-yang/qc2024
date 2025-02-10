package com.tecpie.platform.plan_item.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.TaskConstants;
import com.tecpie.platform.common.enums.BusinessTypeEnum;
import com.tecpie.platform.common.enums.PlanItemExecuteStatusEnum;
import com.tecpie.platform.common.enums.YesOrNoEnum;
import com.tecpie.platform.common.feign.advance.AdvanceNoticeFeignClient;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.notice.api.command.save.AdvanceNoticeSaveCommand;
import com.tecpie.platform.notice.api.command.save.AdvanceParamCommand;
import com.tecpie.platform.plan.entity.Plan;
import com.tecpie.platform.plan_item.api.command.query.PlanItemQueryCommand;
import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.mapper.PlanItemMapper;
import com.tecpie.platform.plan_item.service.PlanItemService;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 停电计划项表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PlanItemServiceImpl extends BaseServiceImpl<PlanItemMapper, PlanItem> implements PlanItemService {

    private final AdvanceNoticeFeignClient advanceNoticeFeignClient;
    private final TaskService taskService;

    public PlanItemServiceImpl(AdvanceNoticeFeignClient advanceNoticeFeignClient, TaskService taskService) {
        this.advanceNoticeFeignClient = advanceNoticeFeignClient;
        this.taskService = taskService;
    }

    @Override
    public PlanItem selectExtensionById(Serializable id) {
        PlanItem entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电计划项表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<PlanItem> searchExtensionPageList(PlanItemQueryCommand command) {
        command.initDateParam();
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<PlanItem> searchListByPlanId(Serializable planId) {
        PlanItemQueryCommand command = new PlanItemQueryCommand();
        command.setPlanId(planId);
        return this.searchExtensionPageList(command);
    }

    @Override
    public void savePlanItem(Serializable planId, List<PlanItem> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                item.setPlanId(planId);
                item.setExecuteStatus(PlanItemExecuteStatusEnum.WGL.getCode());
                // 默认版本号
                item.setVersion(TaskConstants.DEFAULT_VERSION);
            });
            this.saveBatch(list);
        }
    }

    @Override
    public Serializable savePlanItem(PlanItem entity) {
        // 默认未读
        entity.setExecuteStatus(PlanItemExecuteStatusEnum.WGL.getCode());
        // 默认版本号
        if (ObjectUtils.isEmpty(entity.getVersion())) {
            entity.setVersion(TaskConstants.DEFAULT_VERSION);
        }
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updatePlanItem(Serializable id, PlanItem entity) {
        PlanItem existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电计划项目[%s]不存在", id));
        }
        entity.setId(id);
        this.updateById(entity);
        // 获取更新完之后的结果，并判断更新前后是否发生变化，如果发生变化进行version+1
        PlanItem afterUpdate = baseMapper.selectById(id);
        if (
            // 工程编号
                !existEntity.getProjectCode().equals(afterUpdate.getProjectCode())
                        // 工程账号 不相等
                        || !existEntity.getProjectAccount().equals(afterUpdate.getProjectAccount())
                        // 工程名称 不相等
                        || !existEntity.getProjectName().equals(afterUpdate.getProjectName())
                        // 项目类型 不相等
                        || !existEntity.getProjectType().equals(afterUpdate.getProjectType())
                        // 停役时间 不相等
                        || !existEntity.getStartTime().isEqual(afterUpdate.getStartTime())
                        //  复役时间 不相等
                        || !existEntity.getEndTime().isEqual(afterUpdate.getEndTime())
                        // 施工班组 不相等
                        || !existEntity.getConstructionTeam().equals(afterUpdate.getConstructionTeam())
                        // 变电站/线路名称 不相等
                        || !existEntity.getStationLineName().equals(afterUpdate.getStationLineName())
                        // 主要工作内容 不相等
                        || !existEntity.getJobContent().equals(afterUpdate.getJobContent())
                        // 旧停役设备为空但新的不为空
                        || (StringUtils.isBlank(existEntity.getCosDevice()) && StringUtils.isNotBlank(afterUpdate.getCosDevice()))
                        // 旧停役设备不为空但新的为空
                        || (StringUtils.isNotBlank(existEntity.getCosDevice()) && StringUtils.isBlank(afterUpdate.getCosDevice()))
                        // 新旧都不为空，但是不相等的
                        || (StringUtils.isNotBlank(existEntity.getCosDevice()) && StringUtils.isNotBlank(afterUpdate.getCosDevice()) && !existEntity.getCosDevice().equals(afterUpdate.getCosDevice()))
        ) {
            // 进行version+1操作
            lambdaUpdate().eq(PlanItem::getId, id).set(PlanItem::getVersion, afterUpdate.getVersion() + 1).update();
        }
    }

    @Override
    public void updatePlanItem(Serializable planId, List<PlanItem> list) {
        // 保存或更新停电通知用户
        if (!CollectionUtils.isEmpty(list)) {
            // 根据工程编号获取计划项的数据
            PlanItemQueryCommand planItemCommand = new PlanItemQueryCommand();
            planItemCommand.setProjectCodeList(list.stream().map(PlanItem::getProjectCode).collect(Collectors.toList()));
            List<PlanItem> planItemDbList = this.searchExtensionPageList(planItemCommand);
            Map<String, PlanItem> planItemMap = Maps.newHashMap();
            planItemDbList.forEach(item -> planItemMap.put(item.getProjectCode(), item));
            // 需要告警的数据
            List<PlanItem> planItemAdvanceList = Lists.newArrayList();
            // 循环源数据
            list.forEach(item -> {
                if (ObjectUtils.isEmpty(item.getId())) {
                    item.setPlanId(planId);
                    // 工程编号, 一般是导入，每次id都会为null
                    PlanItem planItem = planItemMap.get(item.getProjectCode());
                    if (ObjectUtils.isNotEmpty(planItem)) {
                        // 如果计划ID不相等
                        if (!planId.equals(planItem.getPlanId())) {
                            Plan plan = planItem.getPlan();
                            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("工程编号[%s]在[%s]中已存在", item.getProjectCode(), plan.getTitle()));
                        }
                        item.setId(planItem.getId());
                        item.setExecuteStatus(planItem.getExecuteStatus());
                        item.setVersion(planItem.getVersion());
                    } else {
                        item.setExecuteStatus(PlanItemExecuteStatusEnum.WGL.getCode());
                        // 默认版本号
                        item.setVersion(TaskConstants.DEFAULT_VERSION);
                    }
                } else {
                    // 判断时间是否变更，如果变更需要预警
                    // 这里获取的是数据库里的停电计划项目
                    PlanItem planItem = planItemMap.get(item.getProjectCode());
                    // 待执行和执行中才需要预警
                    String executeStatus = item.getExecuteStatus();
                    if (executeStatus.equals(PlanItemExecuteStatusEnum.YWC.getCode()) || executeStatus.equals(PlanItemExecuteStatusEnum.YGL.getCode())) {
                        // 检索所有planItemId不为空的数据，与数据库数据对比判断，停用时间和复役时间是否有改变
                        if (!item.getStartTime().isEqual(planItem.getStartTime()) || !item.getEndTime().isEqual(planItem.getEndTime())) {
                            // 1、预警前先将项目状态变成为取消
                            // 2、数据做新的拷贝并加入
                            PlanItem newItem = new PlanItem();
                            newItem.copy(item);
                            newItem.setId(null);
                            item.setStatus(YesOrNoEnum.NO.getCode());
                            // 原记录时间恢复成修改之前的
                            newItem.setStartTime(planItem.getStartTime());
                            newItem.setEndTime(planItem.getEndTime());
                            newItem.setVersion(TaskUtil.getNewVersion(newItem.getVersion()));
                            planItemAdvanceList.add(newItem);
                            this.savePlanItem(newItem);
                        }
                    }
                }
            });
            // 级联操作
            this.cascadeUpdate(this.baseMapper.selectList(new LambdaQueryWrapper<PlanItem>().eq(PlanItem::getPlanId, planId)), list);
            // 预警处理逻辑
            if (!CollectionUtils.isEmpty(planItemAdvanceList)) {
                ThreadUtil.execAsync(() -> {
                    try {
                        this.planItemChangeAdvance(planItemAdvanceList);
                    } catch (Exception e) {
                        log.error("======= 保存计划变更告警失败  ======", e);
                    }
                });
            }
        }
    }

    @Override
    public Map<String, Integer> planItemChangeAdvance(List<PlanItem> planItemList) {
        Map<String, Integer> resultMap = Maps.newHashMap();
        try {
            if (CollectionUtils.isEmpty(planItemList)) {
                throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, "停电计划项数据不能为空，请检查");
            }
            List<AdvanceParamCommand> addAdvanceList = Lists.newArrayList();
            // 循环需要告警的数据
            for (PlanItem entity : planItemList) {
                // 需要添加告警
                addAdvanceList.add(AdvanceParamCommand.builder().businessId(entity.getId()).businessCode(entity.getProjectCode())
                        .accountNumber(entity.getProjectAccount()).startTime(entity.getStartTime().atStartOfDay()).endTime(entity.getEndTime().atStartOfDay()).build());
            }
            // 保存预警信息
            resultMap = advanceNoticeFeignClient.savePlanChangeAdvance(AdvanceNoticeSaveCommand.builder()
                    .businessType(BusinessTypeEnum.PLAN_MODEL.getCode()).businessList(addAdvanceList).build()).getData();
        } catch (Exception e) {
            log.error("======= 计划变更告警保存失败  ======", e);
        }
        return resultMap;
    }

    @Override
    public void cancelByIdList(List<Serializable> planItemIdList) {
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(planItemIdList)) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "请传入停电项id");
        }
        Long count = taskService.lambdaQuery().in(Task::getPlanItemId, planItemIdList).count();
        if (count > 0) {
            throw BusinessException.build(SystemError.PARAM_NOT_CHECKED_ERROR, "停电计划项目被关联，无法取消");
        }
        // 校验通过则进行取消的更新操作，记录取消时的时间
        lambdaUpdate()
                .in(PlanItem::getId, planItemIdList)
                .set(PlanItem::getExecuteStatus, PlanItemExecuteStatusEnum.YQX.getCode())
                .set(PlanItem::getCancelTime, LocalDate.now())
                /**
                 * change 2023-09-13
                 * 停电计划项修改为关联/未关联两项
                 * 所以在停电计划页面不在具有取消按钮，修改为删除/批量删除
                 */
                .set(PlanItem::getDeleted, 1)
                .update();
    }
}