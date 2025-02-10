package com.tecpie.platform.plan.service;

import com.tecpie.platform.plan.api.command.query.PlanQueryCommand;
import com.tecpie.platform.plan.api.command.query.PlanStatisticsCommand;
import com.tecpie.platform.plan.api.resource.PlanStatisticsResource;
import com.tecpie.platform.plan.entity.Plan;
import com.tecpie.platform.plan.mapper.PlanMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 停电计划表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
public interface PlanService extends IBaseService<PlanMapper, Plan> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return Plan
     */
    Plan selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<Plan>
     */
    List<Plan> searchExtensionPageList(PlanQueryCommand command);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable savePlan(Plan entity);

    /**
     * 更新
     *
     * @param id
     * @param entity
     */
    void updatePlan(Serializable id, Plan entity);

    /**
     * 更新计划年月及标题信息
     *
     * @param id        主键id
     * @param planMonth 月份
     * @param title     标题
     */
    void updatePlanMonth(Serializable id, LocalDate planMonth, String title);

    /**
     * 导入停电计划项信息
     *
     * @param file 文件对象
     * @return List<String>
     */
    List<String> importPlan(MultipartFile file) throws IOException;

    /**
     * 首页-停电计划统计
     *
     * @param planStatisticsCommand 统计检索条件
     * @return {@link PlanStatisticsResource}
     */
    PlanStatisticsResource homePageStatistics(PlanStatisticsCommand planStatisticsCommand);


    /**
     * 按 ID 删除计划,同时会删除计划项
     * 如果改计划下有计划被停电通知关联，则提示不可删除
     *
     * @param id 编号
     */
    void deletePlanById(Serializable id);
}