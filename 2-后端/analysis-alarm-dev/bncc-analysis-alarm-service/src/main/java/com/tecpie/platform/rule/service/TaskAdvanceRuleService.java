package com.tecpie.platform.rule.service;

import com.tecpie.platform.rule.api.command.query.TaskAdvanceRuleQueryCommand;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.mapper.TaskAdvanceRuleMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 告警规则维护 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
public interface TaskAdvanceRuleService extends IBaseService<TaskAdvanceRuleMapper, TaskAdvanceRule> {

    /**
     * 获取详情信息
     *
     * @param id 主键ID
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskAdvanceRule>
     */
    List<TaskAdvanceRule> searchExtensionPageList(TaskAdvanceRuleQueryCommand command);

    /**
     * 根据规则编号获取告警规则
     *
     * @param ruleCode 规则编号
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule searchByRuleCode(String ruleCode);

    /**
     * 获取下达风险预警规则
     *
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule getCarrierReleaseRule();

    /**
     * 获取下达超时预警规则
     *
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule getReleaseTimeOutRule();

    /**
     * 获取反馈超期预警规则
     *
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule getFeedbackTimeOutRule();

    /**
     * 获取重复停电预警规则
     *
     * @return Map<String, Integer>
     */
    Map<String, Integer> getRepeatPowerCutRule();

    /**
     * 保存
     *
     * @param entity 数据对象
     * @return Serializable
     */
    Serializable saveTaskAdvanceRule(TaskAdvanceRule entity);

    /**
     * 更新
     *
     * @param id     主键ID
     * @param entity 数据对象
     */
    void updateTaskAdvanceRule(Serializable id, TaskAdvanceRule entity);

}