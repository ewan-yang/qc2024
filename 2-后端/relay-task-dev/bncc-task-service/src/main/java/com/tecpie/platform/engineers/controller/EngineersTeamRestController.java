package com.tecpie.platform.engineers.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.engineers.api.EngineersTeamRestApi;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamPageQueryCommand;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamQueryCommand;
import com.tecpie.platform.engineers.api.command.save.EngineersTeamSaveCommand;
import com.tecpie.platform.engineers.api.command.update.EngineersTeamUpdateCommand;
import com.tecpie.platform.engineers.api.resource.EngineersTeamResource;
import com.tecpie.platform.engineers.controller.assembler.command.save.EngineersTeamSaveCommandAssembler;
import com.tecpie.platform.engineers.controller.assembler.command.update.EngineersTeamUpdateCommandAssembler;
import com.tecpie.platform.engineers.controller.assembler.resource.EngineersTeamResourceAssembler;
import com.tecpie.platform.engineers.entity.EngineersTeam;
import com.tecpie.platform.engineers.service.EngineersTeamService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 工程队表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/engineersTeam")
public class EngineersTeamRestController extends BaseController<EngineersTeamService, EngineersTeam, EngineersTeamResource> implements EngineersTeamRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<EngineersTeamResource> getEngineersTeam(@PathVariable("id") Serializable id) {
        EngineersTeam entity = this.baseService.selectExtensionById(id);
        return Result.success(EngineersTeamResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<EngineersTeamResource>> searchEngineersTeamList(@RequestBody EngineersTeamQueryCommand command) {
        List<EngineersTeam> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(EngineersTeamResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<EngineersTeamResource>> searchEngineersTeamPage(@RequestBody EngineersTeamPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(EngineersTeam.class, command));
        // 查询分页数据
        List<EngineersTeam> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, EngineersTeamResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存工程队表数据
     */
    @Override
    @PostMapping
    public Result<Serializable> saveEngineersTeam(@Valid @RequestBody EngineersTeamSaveCommand command) {
        return Result.success(this.baseService.saveEngineersTeam(EngineersTeamSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新工程队表数据
     */
    @Override
    @PutMapping("/{id}")
    public Result updateEngineersTeamById(@PathVariable("id") Serializable id, @Valid @RequestBody EngineersTeamUpdateCommand command) {
        this.baseService.updateEngineersTeam(id, EngineersTeamUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除工程队表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteEngineersTeamById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        return Result.success();
    }

    @Override
    protected EngineersTeamResource toResource(EngineersTeam entity) {
        return EngineersTeamResourceAssembler.INSTANCE.parse(entity);
    }

}