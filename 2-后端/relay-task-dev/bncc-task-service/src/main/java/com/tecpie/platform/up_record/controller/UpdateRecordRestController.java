package com.tecpie.platform.up_record.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.up_record.api.UpdateRecordRestApi;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordPageQueryCommand;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordQueryCommand;
import com.tecpie.platform.up_record.api.resource.UpdateRecordResource;
import com.tecpie.platform.up_record.controller.assembler.resource.UpdateRecordResourceAssembler;
import com.tecpie.platform.up_record.entity.UpdateRecord;
import com.tecpie.platform.up_record.service.UpdateRecordService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 数据修改记录表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/updateRecord")
public class UpdateRecordRestController extends BaseController<UpdateRecordService, UpdateRecord, UpdateRecordResource> implements UpdateRecordRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<UpdateRecordResource> getUpdateRecord(@PathVariable("id") Serializable id) {
        UpdateRecord entity = this.baseService.selectExtensionById(id);
        return Result.success(UpdateRecordResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<UpdateRecordResource>> searchUpdateRecordList(@RequestBody UpdateRecordQueryCommand command) {
        List<UpdateRecord> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(UpdateRecordResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<UpdateRecordResource>> searchUpdateRecordPage(@RequestBody UpdateRecordPageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(UpdateRecord.class, command));
        // 查询分页数据
        List<UpdateRecord> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, UpdateRecordResourceAssembler.INSTANCE.parseList(entityList));
    }

    @Override
    protected UpdateRecordResource toResource(UpdateRecord entity) {
        return UpdateRecordResourceAssembler.INSTANCE.parse(entity);
    }

}