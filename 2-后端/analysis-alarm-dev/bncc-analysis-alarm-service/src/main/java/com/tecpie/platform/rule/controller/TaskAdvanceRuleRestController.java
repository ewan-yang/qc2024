package com.tecpie.platform.rule.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.AnalysisAlarmConstants;
import com.tecpie.platform.rule.api.TaskAdvanceRuleRestApi;
import com.tecpie.platform.rule.api.command.query.TaskAdvanceRulePageQueryCommand;
import com.tecpie.platform.rule.api.command.query.TaskAdvanceRuleQueryCommand;
import com.tecpie.platform.rule.api.command.save.TaskAdvanceRuleSaveCommand;
import com.tecpie.platform.rule.api.command.update.TaskAdvanceRuleUpdateCommand;
import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
import com.tecpie.platform.rule.controller.assembler.command.save.TaskAdvanceRuleSaveCommandAssembler;
import com.tecpie.platform.rule.controller.assembler.command.update.TaskAdvanceRuleUpdateCommandAssembler;
import com.tecpie.platform.rule.controller.assembler.resource.TaskAdvanceRuleResourceAssembler;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.platform.rule.service.TaskAdvanceRuleService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 告警规则维护 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/taskAdvanceRule")
public class TaskAdvanceRuleRestController extends BaseController<TaskAdvanceRuleService, TaskAdvanceRule, TaskAdvanceRuleResource> implements TaskAdvanceRuleRestApi {

    /**
     * 根据ID查询详情信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询详情信息")
    public Result<TaskAdvanceRuleResource> getTaskAdvanceRule(@PathVariable("id") Serializable id) {
        TaskAdvanceRule entity = this.baseService.selectExtensionById(id);
        return Result.success(TaskAdvanceRuleResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @PostMapping("/list")
    @Operation(summary = "根据筛选条件检索列表数据")
    public Result<List<TaskAdvanceRuleResource>> searchTaskAdvanceRuleList(@RequestBody TaskAdvanceRuleQueryCommand command) {
        List<TaskAdvanceRule> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(TaskAdvanceRuleResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "根据筛选条件检索分页列表数据")
    public Result<PagedResult<TaskAdvanceRuleResource>> searchTaskAdvanceRulePage(@RequestBody TaskAdvanceRulePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(TaskAdvanceRule.class, command));
        // 查询分页数据
        List<TaskAdvanceRule> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, TaskAdvanceRuleResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 获取下达风险预警规则编号
     */
    @Override
    @GetMapping("/getCarrierReleaseRule")
    public Result<TaskAdvanceRuleResource> getCarrierReleaseRule() {
        TaskAdvanceRule entity = this.baseService.getCarrierReleaseRule();
        return Result.success(TaskAdvanceRuleResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 获取下达超时预警规则编号
     */
    @Override
    @GetMapping("/getReleaseTimeOutRule")
    public Result<TaskAdvanceRuleResource> getReleaseTimeOutRule() {
        TaskAdvanceRule entity = this.baseService.getReleaseTimeOutRule();
        return Result.success(TaskAdvanceRuleResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 获取反馈超期预警规则
     */
    @Override
    @GetMapping("/getFeedbackTimeOutRule")
    public Result<TaskAdvanceRuleResource> getFeedbackTimeOutRule() {
        TaskAdvanceRule entity = this.baseService.getFeedbackTimeOutRule();
        return Result.success(TaskAdvanceRuleResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 获取重复停电预警规则编号
     */
    @Override
    @GetMapping("/getRepeatPowerCutRule")
    public Result<Map<String, Integer>> getRepeatPowerCutRule() {
        return Result.success(this.baseService.getRepeatPowerCutRule());
    }

    /**
     * 保存告警规则维护数据
     */
    @PostMapping
    @Operation(summary = "保存告警规则维护数据")
    public Result<Serializable> saveTaskAdvanceRule(@Valid @RequestBody TaskAdvanceRuleSaveCommand command) {
        return Result.success(this.baseService.saveTaskAdvanceRule(TaskAdvanceRuleSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新告警规则维护数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "根据ID更新告警规则维护数据")
    public Result updateTaskAdvanceRuleById(@PathVariable("id") Serializable id, @Valid @RequestBody TaskAdvanceRuleUpdateCommand command) {
        this.baseService.updateTaskAdvanceRule(id, TaskAdvanceRuleUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除告警规则维护数据
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除告警规则维护数据")
    public Result deleteTaskAdvanceRuleById(@PathVariable("id") Serializable id) {
        TaskAdvanceRule taskAdvanceRule = this.baseService.selectExtensionById(id);
        String ruleCode = taskAdvanceRule.getRuleCode();
        if (AnalysisAlarmConstants.CARRIER_RELEASE_RULE_CODE.equals(ruleCode) || AnalysisAlarmConstants.RELEASE_TIME_OUT_RULE_CODE.equals(ruleCode)
                || AnalysisAlarmConstants.RPC_LT_35KV_RULE_CODE.equals(ruleCode) || AnalysisAlarmConstants.RPC_GTE_35KV_RULE_CODE.equals(ruleCode)
                || AnalysisAlarmConstants.FEEDBACK_TIME_OUT_RULE_CODE.equals(ruleCode)) {
            throw BusinessException.build(SystemError.NOT_SUPPORT_OPERATE, String.format("告警规则[%s]至[%s]不允许删除", AnalysisAlarmConstants.CARRIER_RELEASE_RULE_CODE, AnalysisAlarmConstants.FEEDBACK_TIME_OUT_RULE_CODE));
        }
        this.baseService.removeById(id);
        return Result.success();
    }

    @Override
    protected TaskAdvanceRuleResource toResource(TaskAdvanceRule entity) {
        return TaskAdvanceRuleResourceAssembler.INSTANCE.parse(entity);
    }

}