package com.tecpie.platform.user.business.user_message.controller;

import com.github.pagehelper.page.PageMethod;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.user_message.api.UserMessageRestApi;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessagePageQueryCommand;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessageQueryCommand;
import com.tecpie.platform.user.business.user_message.api.command.save.UserMessageSaveCommand;
import com.tecpie.platform.user.business.user_message.api.command.update.UserMessageUpdateCommand;
import com.tecpie.platform.user.business.user_message.api.resource.UserMessageResource;
import com.tecpie.platform.user.business.user_message.controller.assembler.command.save.UserMessageSaveCommandAssembler;
import com.tecpie.platform.user.business.user_message.controller.assembler.command.update.UserMessageUpdateCommandAssembler;
import com.tecpie.platform.user.business.user_message.controller.assembler.resource.UserMessageResourceAssembler;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import com.tecpie.platform.user.business.user_message.service.UserMessageService;
import com.tecpie.starter.jdbc.support.mybatis.business.controller.BaseController;
import com.tecpie.starter.jdbc.util.condition.ConditionUtil;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户消息表 控制器实现
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/userMessage")
public class UserMessageRestController extends BaseController<UserMessageService, UserMessage, UserMessageResource> implements UserMessageRestApi {

    /**
     * 根据ID查询详情信息
     */
    @Override
    @GetMapping("/{id}")
    public Result<UserMessageResource> getUserMessage(@PathVariable("id") Serializable id) {
        UserMessage entity = this.baseService.selectExtensionById(id);
        return Result.success(UserMessageResourceAssembler.INSTANCE.parse(entity));
    }

    /**
     * 根据筛选条件检索列表数据
     */
    @Override
    @PostMapping("/list")
    public Result<List<UserMessageResource>> searchUserMessageList(@RequestBody UserMessageQueryCommand command) {
        command.setUserId(TecpieSecurityUtil.getUser().getId());
        List<UserMessage> entityList = this.baseService.searchExtensionPageList(command);
        return Result.success(UserMessageResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 根据筛选条件检索分页列表数据
     */
    @Override
    @PostMapping("/page")
    public Result<PagedResult<UserMessageResource>> searchUserMessagePage(@RequestBody UserMessagePageQueryCommand command) {
        command.getCondition().setUserId(TecpieSecurityUtil.getUser().getId());
        // 开启分页切面
        PageMethod.startPage(command.getPageIndex(), command.getPageSize(), ConditionUtil.buildOrderBy(UserMessage.class, command));
        // 查询分页数据
        List<UserMessage> entityList = this.baseService.searchExtensionPageList(command.getCondition());
        // 构造分页结果
        return Result.success(entityList, UserMessageResourceAssembler.INSTANCE.parseList(entityList));
    }

    /**
     * 保存用户消息表数据
     */
    @Override
    @PostMapping
    public Result<Serializable> saveUserMessage(@Valid @RequestBody UserMessageSaveCommand command) {
        return Result.success(this.baseService.saveUserMessage(UserMessageSaveCommandAssembler.INSTANCE.parse(command)));
    }

    @Override
    @PostMapping("/batchSave")
    public Result<Boolean> batchSaveUserMessage(@RequestBody List<UserMessageSaveCommand> commands) {
        return Result.success(this.baseService.saveUserMessageBatch(UserMessageSaveCommandAssembler.INSTANCE.parseList(commands)));
    }

    /**
     * 根据ID更新用户消息表数据
     */
    @Override
    @PutMapping("/{id}")
    public Result updateUserMessageById(@PathVariable("id") Serializable id, @Valid @RequestBody UserMessageUpdateCommand command) {
        this.baseService.updateUserMessage(id, UserMessageUpdateCommandAssembler.INSTANCE.parse(command));
        return Result.success();
    }

    /**
     * 根据ID删除用户消息表数据
     */
    @Override
    @DeleteMapping("/{id}")
    public Result deleteUserMessageById(@PathVariable("id") Serializable id) {
        this.baseService.removeById(id);
        return Result.success();
    }

    /**
     * 根据ID变更用户消息表状态
     */
    @Override
    @PutMapping("/{id}/status/{status}")
    public Result changeUserMessageStatusById(@PathVariable("id") Serializable id, @PathVariable("status") Integer status) {
        this.baseService.changeUserMessageStatus(id, status);
        return Result.success();
    }

    @Override
    protected UserMessageResource toResource(UserMessage entity) {
        return UserMessageResourceAssembler.INSTANCE.parse(entity);
    }

}
