package com.tecpie.platform.task_user.api;

import com.tecpie.library.common.business.controller.resource.PagedResult;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.task_user.api.command.query.TaskUserPageQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserAssignSaveCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserCancelSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserPrintResource;
import com.tecpie.platform.task_user.api.resource.TaskUserResource;
import com.tecpie.platform.task_user.api.resource.TaskUserSumTotalResource;
import com.tecpie.platform.task_user.api.resource.TaskUserTotalResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电任务用户表 接口定义
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Tag(name = "停电任务用户表接口定义")
@Headers("Accept: application/json")
public interface TaskUserRestApi {

    /**
     * 根据ID查询详情信息
     *
     * @param id 主键ID
     * @return Result<TaskUserResource>
     */
    @Operation(summary = "查询详情信息")
    @RequestLine("GET /api/v1/taskUser/{id}")
    Result<TaskUserResource> getTaskUser(@Param("id") Serializable id);

    /**
     * 根据筛选条件检索列表数据
     *
     * @param command 查询参数
     * @return Result<List < TaskUserResource>>
     */
    @Operation(summary = "检索列表数据")
    @RequestLine("POST /api/v1/taskUser/list")
    Result<List<TaskUserResource>> searchTaskUserList(TaskUserQueryCommand command);

    /**
     * 根据筛选条件检索分页列表数据
     *
     * @param command
     * @return Result<PagedResult < TaskUserResource>>
     */
    @Operation(summary = "检索分页列表数据")
    @RequestLine("POST /api/v1/taskUser/page")
    Result<PagedResult<TaskUserResource>> searchTaskUserPage(TaskUserPageQueryCommand command);

    /**
     * 查询用户最新的反馈时间
     */
    @Operation(summary = "查询用户最新的反馈时间")
    @RequestLine("POST /api/v1/taskUser/selectMaxFeedbackTime")
    Result<Map<String, String>> selectMaxFeedbackTime(List<Serializable> taskUserIdList);

    /**
     * 根据idList获取打印数据
     *
     * @param taskUserIdList 停电任务用户IdList
     * @return Result<PagedResult < TaskUserResource>>
     */
    @Operation(summary = "根据idList获取打印数据")
    @RequestLine("POST /api/v1/taskUser/getPrintData")
    Result<List<TaskUserPrintResource>> getPrintData(List<Serializable> taskUserIdList);

    /**
     * 根据ID删除停电任务用户表数据
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "删除停电任务用户表数据")
    @RequestLine("DELETE /api/v1/taskUser/{id}")
    Result deleteTaskUserById(@Param("id") Serializable id);

    /**
     * 批量删除停电任务用户表数据
     *
     * @param ids
     * @return Result
     */
    @Operation(summary = "批量删除停电任务用户表数据")
    @RequestLine("DELETE /api/v1/taskUser/batchDelete/{ids}")
    Result deleteBatchIds(@Param("ids") String ids);

    /**
     * 批量停电通知用户派发
     *
     * @param command 停电通知用户派发对象
     * @return Result
     */
    @Operation(summary = "批量停电通知用户派发")
    @RequestLine("PUT /api/v1/taskUser/batchAssign")
    Result batchAssign(TaskUserAssignSaveCommand command);

    /**
     * 根据查询条件一键派发停电通知用户
     *
     * @param command 查询参数
     * @return Result
     */
    @Operation(summary = "根据查询条件一键派发停电通知用户")
    @RequestLine("PUT /api/v1/taskUser/oneTouchAssign")
    Result oneTouchAssign(TaskUserQueryCommand command);

    /**
     * 变更派发负责方
     *
     * @param command 停电通知用户派发对象
     * @return Result
     */
    @Operation(summary = "变更派发负责方")
    @RequestLine("PUT /api/v1/taskUser/changeEngineersTeam")
    Result changeEngineersTeam(TaskUserAssignSaveCommand command);

    /**
     * 取消派发
     *
     * @param command
     * @return Result
     */
    @Operation(summary = "取消派发")
    @RequestLine("PUT /api/v1/taskUser/cancel")
    Result cancel(TaskUserCancelSaveCommand command);

    /**
     * 恢复派发，针对那些误操作取消的用户进行状态恢复
     *
     * @param id
     * @return Result
     */
    @Operation(summary = "恢复派发")
    @RequestLine("PUT /api/v1/taskUser/recoverCancel/{id}")
    Result recoverCancel(@Param("id") Serializable id);

    /**
     * 停电任务用户统计信息
     *
     * @param command 统计请求参数
     * @return Result<TaskUserTotalResource>
     */
    @Operation(summary = "停电任务用户统计信息")
    @RequestLine("POST /api/v1/taskUser/total")
    Result<TaskUserTotalResource> getTaskUserTotal(TaskUserTotalQueryCommand command);

    /**
     * 停电任务用户数据汇总
     *
     * @param command 统计请求参数
     * @return Result<TaskUserSumTotalResource>
     */
    @Operation(summary = "停电任务用户数据汇总")
    @RequestLine("POST /api/v1/taskUser/sumTotal")
    Result<TaskUserSumTotalResource> getTaskUserSumTotal(TaskUserTotalQueryCommand command);

}