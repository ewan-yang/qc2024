package com.tecpie.platform.plan.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.TaskConstants;
import com.tecpie.platform.common.enums.PlanItemExecuteStatusEnum;
import com.tecpie.platform.common.util.TaskUtil;
import com.tecpie.platform.plan.api.command.query.PlanQueryCommand;
import com.tecpie.platform.plan.api.command.query.PlanStatisticsCommand;
import com.tecpie.platform.plan.api.resource.PlanStatisticsResource;
import com.tecpie.platform.plan.entity.Plan;
import com.tecpie.platform.plan.mapper.PlanMapper;
import com.tecpie.platform.plan.service.PlanService;
import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.service.PlanItemService;
import com.tecpie.platform.seq.service.SeqGenService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 停电计划表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PlanServiceImpl extends BaseServiceImpl<PlanMapper, Plan> implements PlanService {

    private final PlanItemService planItemService;
    private final SeqGenService seqGenService;

    public static final String PLAN_NOT_EXIST = "停电计划表[%s]不存在";

    public PlanServiceImpl(PlanItemService planItemService, SeqGenService seqGenService) {
        this.planItemService = planItemService;
        this.seqGenService = seqGenService;
    }

    @Override
    public Plan selectExtensionById(Serializable id) {
        Plan entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(PLAN_NOT_EXIST, id));
        }
        return entity;
    }

    @Override
    public List<Plan> searchExtensionPageList(PlanQueryCommand command) {
        command.initDateParam();
        List<Plan> planList = this.baseMapper.searchExtensionPageList(command);
        // 提取所有需要统计状态数据的停电计划id
        List<Serializable> planIdList = planList.stream().map(Plan::getId).collect(Collectors.toList());
        // 查询所有停电计划id对应的停电计划项目
        if (!CollectionUtils.isEmpty(planIdList)) {
            List<PlanItem> planItemList = planItemService.lambdaQuery().in(PlanItem::getPlanId, planIdList).list();
            if (!CollectionUtils.isEmpty(planItemList)) {
                Map<String, List<PlanItem>> planItemMap = planItemList.stream().collect(Collectors.groupingBy(e -> e.getPlanId().toString()));
                // 统计数据
                planList.forEach(t -> t.statusQty(planItemMap.get(t.getId().toString())));
            }
        }
        return planList;
    }

    @Override
    public Serializable savePlan(Plan entity) {
        // 停电计划编号
        entity.setCode(seqGenService.getValue(TaskConstants.PLAN_CODE_SEQ));
        // 根据计划月份判断是否已经存在
        PlanQueryCommand command = new PlanQueryCommand();
        command.setPlanMonth(entity.getPlanMonth().format(DateTimeFormatter.ofPattern(DatePattern.NORM_MONTH_PATTERN)));
        List<Plan> planList = this.searchExtensionPageList(command);
        if (!CollectionUtils.isEmpty(planList)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("[%s]计划已经存在", entity.getPlanMonth().format(DateTimeFormatter.ofPattern(DatePattern.NORM_MONTH_PATTERN))));
        }
        this.save(entity);
        // 保存停电计划项
        planItemService.savePlanItem(entity.getId(), entity.getPlanItemList());
        return entity.getId();
    }

    @Override
    public void updatePlan(Serializable id, Plan entity) {
        Plan existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(PLAN_NOT_EXIST, id));
        }
        entity.setId(id);
        // 更新数据
        this.updateById(entity);
        // 更新停电计划项
        planItemService.updatePlanItem(entity.getId(), entity.getPlanItemList());
    }

    @Override
    public void updatePlanMonth(Serializable id, LocalDate planMonth, String title) {
        Plan existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(PLAN_NOT_EXIST, id));
        }
        existEntity.setTitle(title);
        existEntity.setPlanMonth(planMonth);
        // 更新数据
        this.updateById(existEntity);
    }

    @Override
    public List<String> importPlan(MultipartFile file) throws IOException {
        List<String> resultList = Lists.newArrayList();
        // 读取Excel
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        // 用户Excel第一行的停电计划月份、标题
        List<Object> planExcelList = reader.readRow(1);
        // 计划月份
        LocalDate planMonthLocal = DateUtil.parse(TaskUtil.convertToString(planExcelList.get(0))).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 判断工程编号是不是在数据库中已存在
        List<String> projectCodeList = Lists.newArrayList();
        // 判断工程编号在当前Excel是否有重复
        Map<String, Integer> projectCodeMap = Maps.newHashMap();
        // 根据月份查询停电计划
        PlanQueryCommand command = new PlanQueryCommand();
        command.setPlanMonth(planMonthLocal.format(DateTimeFormatter.ofPattern(DatePattern.NORM_MONTH_PATTERN)));
        List<Plan> planList = this.searchExtensionPageList(command);
        Plan plan = new Plan();
        if (!CollectionUtils.isEmpty(planList)) {
            plan = planList.get(0);
            List<PlanItem> planItemList = planItemService.searchListByPlanId(plan.getId());
            planItemList.forEach(item -> projectCodeList.add(item.getProjectCode()));
        }
        // 标题
        String title = TaskUtil.convertToString(planExcelList.get(1));
        // 需要新增的数据
        List<PlanItem> planItemList = Lists.newArrayList();
        // 读取停电计划项内容
        List<List<Object>> excelList = reader.read(4, reader.getRowCount() - 1);
        int idx = 5;
        for (List<Object> dataList : excelList) {
            // 是否结束循环
            boolean isContinue = false;
            // 工程编号
            String projectCode = TaskUtil.convertToString(dataList.get(0));
            if (StringUtils.isBlank(projectCode)) {
                resultList.add(String.format("第[%s]行工程编号不能为空", idx));
                isContinue = true;
            }
            // 判断工程编号在当前Excel是否有重复
            if (projectCodeMap.containsKey(projectCode)) {
                resultList.add(String.format("第[%s]行跟第[%s]行工程编号[%s]重复，请检查", idx, projectCodeMap.get(projectCode), projectCode));
                isContinue = true;
            } else {
                projectCodeMap.put(projectCode, idx);
            }
            idx++;
            // 只要本次循环有错误信息，则不必要往下面执行
            if (isContinue) {
                continue;
            }
            PlanItem planItem = new PlanItem();
            planItem.setProjectCode(projectCode);
            planItem.setProjectAccount(TaskUtil.convertToString(dataList.get(1)));
            planItem.setProjectName(TaskUtil.convertToString(dataList.get(2)));
            planItem.setProjectType(TaskUtil.convertToString(dataList.get(3)));
            planItem.setStationLineName(TaskUtil.convertToString(dataList.get(4)));
            planItem.setCosDevice(TaskUtil.convertToString(dataList.get(5)));
            planItem.setStartTime(TaskUtil.convertToLocalDate(dataList.get(6)));
            planItem.setEndTime(TaskUtil.convertToLocalDate(dataList.get(7)));
            planItem.setJobContent(TaskUtil.convertToString(dataList.get(8)));
            planItem.setConstructionTeam(TaskUtil.convertToString(dataList.get(9)));
            planItemList.add(planItem);
        }
        // 没有错误信息泽保存数据
        if (CollectionUtils.isEmpty(resultList)) {
            // 保存或更新停电计划及计划项
            plan.setPlanMonth(planMonthLocal);
            plan.setTitle(title);
            plan.setPlanItemList(planItemList);
            if (plan.getId() == null) {
                this.savePlan(plan);
            } else {
                this.updatePlan(plan.getId(), plan);
            }
            // 成功信息返回
            resultList.add(String.format("导入成功，本次共导入%s条停电计划项数据", planItemList.size()));
        }
        return resultList;
    }

    @Override
    public PlanStatisticsResource homePageStatistics(PlanStatisticsCommand planStatisticsCommand) {
        // 检索所有符合时间条件的停电计划项
        List<PlanItem> planItemList = planItemService.lambdaQuery()
                .ge(PlanItem::getStartTime, planStatisticsCommand.getCreateDateBegin())
                .le(PlanItem::getStartTime, planStatisticsCommand.getCreateDateEnd())
                .eq(PlanItem::getStatus, 1)
                .list();
        // 生成该对象仅用于统计
        Plan plan = new Plan();
        plan.statusQty(planItemList);
        PlanStatisticsResource planStatisticsResource = PlanStatisticsResource.builder().build();
        BeanUtils.copyProperties(plan, planStatisticsResource);
        return planStatisticsResource;
    }

    @Override
    public void deletePlanById(Serializable id) {
        // 检索改停电计划下有没有停电计划项被关联
        Long count = planItemService.lambdaQuery()
                .eq(PlanItem::getPlanId, id)
                .eq(PlanItem::getExecuteStatus, PlanItemExecuteStatusEnum.YGL.getCode())
                .count();
        if (count > 0) {
            throw BusinessException.build("该停电计划下已有停电计划项被关联");
        }
        // 如果没有停电计划被关联，则会直接删除停电计划和旗下子项
        baseMapper.deleteById(id);
        // 删除子项
        planItemService.lambdaUpdate().eq(PlanItem::getPlanId, id).set(PlanItem::getDeleted, 1).update();
    }

}