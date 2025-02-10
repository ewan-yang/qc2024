package com.tecpie.platform.file.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.file.api.CommonFileRestApi;
import com.tecpie.platform.file.api.command.query.CommonFilePageQueryCommand;
import com.tecpie.platform.file.api.command.query.CommonFileQueryCommand;
import com.tecpie.platform.file.api.resource.CommonFileResource;
import com.tecpie.platform.file.controller.assembler.resource.CommonFileResourceAssembler;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.service.CommonFileService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 文件和图片表 控制器实现
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/commonFile")
public class CommonFileRestController extends BaseController<CommonFileService, CommonFile, CommonFileResource> implements CommonFileRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<CommonFileResource> getCommonFile(@PathVariable("id") Serializable id) {
        CommonFile entity = this.baseService.selectExtensionById(id);
        return Result.success(CommonFileResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<CommonFileResource>> searchCommonFileList(@RequestBody CommonFileQueryCommand command) {
        List<CommonFile> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(CommonFileResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<CommonFileResource>> searchCommonFilePage(@RequestBody CommonFilePageQueryCommand command) {
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(CommonFile.class, command));
        // 查询分页数据
        List<CommonFile> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, CommonFileResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据ID删除文件和图片表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteCommonFileById(@PathVariable("id") Serializable id) {
        this.baseService.deleteCommonFile(id);
        return Result.success();
    }

    @Override
    protected CommonFileResource toResource(CommonFile entity) {
        return CommonFileResourceAssembler.INSTANCE.parse(entity);
    }

}