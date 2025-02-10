package com.tecpie.platform.rule.service.impl;

import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.AnalysisAlarmConstants;
import com.tecpie.platform.common.feign.seq.SeqGenFeignClient;
import com.tecpie.platform.rule.api.command.query.TaskAdvanceRuleQueryCommand;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.mapper.TaskAdvanceRuleMapper;
import com.tecpie.platform.rule.service.TaskAdvanceRuleService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 告警规则维护 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskAdvanceRuleServiceImpl extends BaseServiceImpl<TaskAdvanceRuleMapper, TaskAdvanceRule> implements TaskAdvanceRuleService {

    private SeqGenFeignClient seqGenFeignClient;

    @Autowired
    public void setTaskAdvanceRuleServiceImpl(SeqGenFeignClient seqGenFeignClient) {
        this.seqGenFeignClient = seqGenFeignClient;
    }

    @Override
    public TaskAdvanceRule selectExtensionById(Serializable id) {
        TaskAdvanceRule entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("告警规则维护[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<TaskAdvanceRule> searchExtensionPageList(TaskAdvanceRuleQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public TaskAdvanceRule searchByRuleCode(String ruleCode) {
        TaskAdvanceRuleQueryCommand command = new TaskAdvanceRuleQueryCommand();
        command.setRuleCode(ruleCode);
        List<TaskAdvanceRule> advanceRuleList = this.searchExtensionPageList(command);
        if (CollectionUtils.isEmpty(advanceRuleList)) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("告警规则维护[%s]不存在", ruleCode));
        }
        return advanceRuleList.get(0);
    }

    @Override
    public TaskAdvanceRule getCarrierReleaseRule() {
        return this.searchByRuleCode(AnalysisAlarmConstants.CARRIER_RELEASE_RULE_CODE);
    }

    @Override
    public TaskAdvanceRule getReleaseTimeOutRule() {
        return this.searchByRuleCode(AnalysisAlarmConstants.RELEASE_TIME_OUT_RULE_CODE);
    }

    @Override
    public TaskAdvanceRule getFeedbackTimeOutRule() {
        return this.searchByRuleCode(AnalysisAlarmConstants.FEEDBACK_TIME_OUT_RULE_CODE);
    }

    @Override
    public Map<String, Integer> getRepeatPowerCutRule() {
        Map<String, Integer> ruleDaysMap = Maps.newHashMap();
        // 35KV以下的
        TaskAdvanceRule entity = this.searchByRuleCode(AnalysisAlarmConstants.RPC_LT_35KV_RULE_CODE);
        String rule = entity.getParams1();
        if (StringUtils.isNotBlank(rule)) {
            Integer days = entity.getDays();
            List<String> ruleList = Arrays.asList(rule.split(","));
            ruleList.forEach(item -> ruleDaysMap.put(item, days));
        }
        // 35KV及之上的
        entity = this.searchByRuleCode(AnalysisAlarmConstants.RPC_GTE_35KV_RULE_CODE);
        rule = entity.getParams1();
        if (StringUtils.isNotBlank(rule)) {
            Integer days = entity.getDays();
            List<String> ruleList = Arrays.asList(rule.split(","));
            ruleList.forEach(item -> ruleDaysMap.put(item, days));
        }
        return ruleDaysMap;
    }

    @Override
    public Serializable saveTaskAdvanceRule(TaskAdvanceRule entity) {
        if (StringUtils.isBlank(entity.getRuleCode())) {
            entity.setRuleCode(seqGenFeignClient.getSeqValue(AnalysisAlarmConstants.RULE_CODE_GEN).getData());
        }
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updateTaskAdvanceRule(Serializable id, TaskAdvanceRule entity) {
        TaskAdvanceRule existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("告警规则维护[%s]不存在", id));
        }
        entity.setId(id);
        this.updateById(entity);
    }

}