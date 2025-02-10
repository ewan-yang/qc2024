package com.tecpie.platform.plan_item.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.platform.common.enums.PlanItemExecuteStatusEnum;
import com.tecpie.platform.plan_item.api.PlanItemRestApi;
import com.tecpie.platform.plan_item.api.command.query.PlanItemPageQueryCommand;
import com.tecpie.platform.plan_item.api.command.query.PlanItemQueryCommand;
import com.tecpie.platform.plan_item.api.command.save.PlanItemSaveCommand;
import com.tecpie.platform.plan_item.api.command.update.PlanItemUpdateCommand;
import com.tecpie.platform.plan_item.api.resource.PlanItemResource;
import com.tecpie.platform.plan_item.controller.assembler.command.save.PlanItemSaveCommandAssembler;
import com.tecpie.platform.plan_item.controller.assembler.command.update.PlanItemUpdateCommandAssembler;
import com.tecpie.platform.plan_item.controller.assembler.resource.PlanItemResourceAssembler;
import com.tecpie.platform.plan_item.entity.PlanItem;
import com.tecpie.platform.plan_item.service.PlanItemService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 停电计划项表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/planItem")
public class PlanItemRestController extends BaseController<PlanItemService, PlanItem, PlanItemResource> implements PlanItemRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<PlanItemResource> getPlanItem(@PathVariable("id") Serializable id) {
        PlanItem entity = this.baseService.selectExtensionById(id);
        return Result.success(PlanItemResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<PlanItemResource>> searchPlanItemList(@RequestBody PlanItemQueryCommand command) {
        List<PlanItem> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(PlanItemResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<PlanItemResource>> searchPlanItemPage(@RequestBody PlanItemPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(PlanItem.class, command));
        // 查询分页数据
        List<PlanItem> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, PlanItemResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据ID删除停电计划项表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deletePlanItemById(@PathVariable("id") Serializable id) {
        // 检索停电计划项是否已关联，如果已关联，不允许删除
        PlanItem planItem = baseService.getById(id);
        if (PlanItemExecuteStatusEnum.YGL.getCode().equals(planItem.getExecuteStatus())) {
            throw BusinessException.build("该停电计划已关联，不可删除");
        }
        this.baseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/cancelByIdList")
    @Operation(summary = "根据停电计划项目id集合取消相应的停电计划项目")
    public Result<Void> cancelByIdList(@RequestBody List<Serializable> planItemIdList) {
        this.baseService.cancelByIdList(planItemIdList);
        return Result.success();
    }

    /**
     * 保存停电计划项目数据
     */
    @PostMapping
    @Operation(summary = "保存停电计划项目数据")
    public Result<Serializable> savePlanItem(@Valid @RequestBody PlanItemSaveCommand command) {
        return Result.success(this.baseService.savePlanItem(PlanItemSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新停电计划项目数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "根据ID更新停电计划项目数据")
    public Result<Void> updatePlanItemById(@PathVariable("id") Serializable id, @Valid @RequestBody PlanItemUpdateCommand command) {
        this.baseService.updatePlanItem(id, PlanItemUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }


    @Override
    protected PlanItemResource toResource(PlanItem entity) {
        return PlanItemResourceAssembler.INSTANCE.parse(entity);
    }

}