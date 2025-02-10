package com.tecpie.platform.user.business.user_message.api;

import com.tecpie.platform.user.business.user_message.api.command.save.UserMessageSaveCommand;
import com.tecpie.platform.user.business.user_message.api.command.update.UserMessageUpdateCommand;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessageQueryCommand;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessagePageQueryCommand;
import com.tecpie.platform.user.business.user_message.api.resource.UserMessageResource;
import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * 用户消息表 接口定义
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Tag(name = "用户消息表接口定义")
@Headers("Accept: application/json")
public interface UserMessageRestApi {

  /**
   * 根据ID查询详情信息
   *
   * @param id
   * @return Result<UserMessageResource>
   */
  @Operation(summary = "查询详情信息")
  @RequestLine("GET /api/v1/userMessage/{id}")
  Result<UserMessageResource> getUserMessage(@Param("id") Serializable id);

  /**
   * 根据筛选条件检索列表数据
   *
   * @param command
   * @return Result<List<UserMessageResource>>
   */
  @Operation(summary = "检索列表数据")
  @RequestLine("POST /api/v1/userMessage/list")
  Result<List<UserMessageResource>> searchUserMessageList(UserMessageQueryCommand command);

  /**
   * 根据筛选条件检索分页列表数据
   *
   * @param command
   * @return Result<PagedResult<UserMessageResource>>
   */
  @Operation(summary = "检索分页列表数据")
  @RequestLine("POST /api/v1/userMessage/page")
  Result<PagedResult<UserMessageResource>> searchUserMessagePage(UserMessagePageQueryCommand command);

  /**
   * 保存用户消息表数据
   *
   * @param command
   * @return Result<Serializable>
   */
  @Operation(summary = "保存用户消息表数据")
  @RequestLine("POST /api/v1/userMessage")
  Result<Serializable> saveUserMessage(UserMessageSaveCommand command);

  /**
   * 批量保存用户消息表数据
   *
   */
  @Operation(summary = "批量保存用户消息表数据")
  @RequestLine("POST /api/v1/userMessage/batchSave")
  Result<Boolean> batchSaveUserMessage(List<UserMessageSaveCommand> commands);

  /**
   * 根据ID更新用户消息表数据
   *
   * @param command
   * @return Result
   */
  @Operation(summary = "更新用户消息表数据")
  @RequestLine("PUT /api/v1/userMessage/{id}")
  Result updateUserMessageById(@Param("id") Serializable id, UserMessageUpdateCommand command);

  /**
   * 根据ID删除用户消息表数据
   *
   * @param id
   * @return Result
   */
  @Operation(summary = "删除用户消息表数据")
  @RequestLine("DELETE /api/v1/userMessage/{id}")
  Result deleteUserMessageById(@Param("id") Serializable id);

  /**
   * 根据ID变更用户消息表状态
   *
   * @param id
   * @param status
   * @return String
   */
  @Operation(summary = "变更用户消息表状态")
  @RequestLine("PUT /api/v1/userMessage/{id}/status/{status}")
  Result changeUserMessageStatusById(@Param("id") Serializable id, @Param("status") Integer status);

}