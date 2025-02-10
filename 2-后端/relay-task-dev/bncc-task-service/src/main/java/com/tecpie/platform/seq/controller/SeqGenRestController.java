package com.tecpie.platform.seq.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.seq.api.SeqGenRestApi;
import com.tecpie.platform.seq.api.command.query.SeqGenPageQueryCommand;
import com.tecpie.platform.seq.api.command.query.SeqGenQueryCommand;
import com.tecpie.platform.seq.api.command.save.SeqGenSaveCommand;
import com.tecpie.platform.seq.api.command.update.SeqGenUpdateCommand;
import com.tecpie.platform.seq.api.resource.SeqGenResource;
import com.tecpie.platform.seq.controller.assembler.command.save.SeqGenSaveCommandAssembler;
import com.tecpie.platform.seq.controller.assembler.command.update.SeqGenUpdateCommandAssembler;
import com.tecpie.platform.seq.controller.assembler.resource.SeqGenResourceAssembler;
import com.tecpie.platform.seq.entity.SeqGen;
import com.tecpie.platform.seq.service.SeqGenService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 序列表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/seqGen")
public class SeqGenRestController extends BaseController<SeqGenService, SeqGen, SeqGenResource> implements SeqGenRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<SeqGenResource> getSeqGen(@PathVariable("id") Serializable id) {
        SeqGen entity = this.baseService.selectExtensionById(id);
        return Result.success(SeqGenResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<SeqGenResource>> searchSeqGenList(@RequestBody SeqGenQueryCommand command) {
        List<SeqGen> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(SeqGenResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<SeqGenResource>> searchSeqGenPage(@RequestBody SeqGenPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(SeqGen.class, command));
        // 查询分页数据
        List<SeqGen> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, SeqGenResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据业务类型获取序列数据
     */
    @Override
    @PostMapping("/getData/{businessType}")
    public Result<SeqGenResource> getSeqByBusinessType(@PathVariable("businessType") String businessType) {
        return Result.success(SeqGenResourceAssembler.INSTANCE.parse(this.baseService.getSeqByBusinessType(businessType)));
    }

    /**
     * 根据业务类型获取序列值
     */
    @Override
    @PostMapping("/getValue/{businessType}")
    public Result<String> getSeqValue(@PathVariable("businessType") String businessType) {
        return Result.success(this.baseService.getValue(businessType));
    }

    /**
     * 保存序列表数据
     */
    @Override
    @PostMapping
    public Result<Serializable> saveSeqGen(@Valid @RequestBody SeqGenSaveCommand command) {
        return Result.success(this.baseService.saveSeqGen(SeqGenSaveCommandAssembler.INSTANCE.parse(command)));
    }

    /**
     * 根据ID更新序列表数据
     */
    @Override
    @PutMapping("/{id}")
    public Result updateSeqGenById(@PathVariable("id") Serializable id, @Valid @RequestBody SeqGenUpdateCommand command) {
        this.baseService.updateSeqGen(id, SeqGenUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除序列表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteSeqGenById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        return Result.success();
    }

    /**
     * 根据ID变更序列表状态
     */
    @Override
    @PutMapping("/{id}/status/{status}")
    public Result changeSeqGenStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
        this.baseService.changeSeqGenStatus(id, status);
        return Result.success();
    }

    @Override
    protected SeqGenResource toResource(SeqGen entity) {
        return SeqGenResourceAssembler.INSTANCE.parse(entity);
    }

}