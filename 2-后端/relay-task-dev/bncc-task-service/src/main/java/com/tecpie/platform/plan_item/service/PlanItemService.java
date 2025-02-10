package com.tecpie.platform.plan_item.service;

import com.tecpie.platform.plan_item.api.command.query.PlanItemQueryCommand;
import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.mapper.PlanItemMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电计划项表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
public interface PlanItemService extends IBaseService<PlanItemMapper, PlanItem> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return PlanItem
     */
    PlanItem selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<PlanItem>
     */
    List<PlanItem> searchExtensionPageList(PlanItemQueryCommand command);

    /**
     * 根据停电计划查询计划项List
     *
     * @param planId 计划ID
     * @return List<PlanItem>
     */
    List<PlanItem> searchListByPlanId(Serializable planId);

    /**
     * 保存
     *
     * @param planId 计划ID
     * @param list   数据List
     */
    void savePlanItem(Serializable planId, List<PlanItem> list);

    /**
     * 保存停电计划项目数据，单次新增
     */
    Serializable savePlanItem(PlanItem planItem);

    /**
     * 更新
     *
     * @param id
     * @param entity
     */
    void updatePlanItem(Serializable id, PlanItem entity);

    /**
     * 更新
     *
     * @param planId 计划ID
     * @param list   数据List
     */
    void updatePlanItem(Serializable planId, List<PlanItem> list);

    /**
     * 停电计划变更告警
     *
     * @param planItemList 停电计划项List
     * @return Map<String, Integer>
     */
    Map<String, Integer> planItemChangeAdvance(List<PlanItem> planItemList);

    /**
     * 根据停电计划项目id集合取消相应的停电计划项目
     * @param planItemIdList 停电项目id集合
     */
    void cancelByIdList(List<Serializable> planItemIdList);
}