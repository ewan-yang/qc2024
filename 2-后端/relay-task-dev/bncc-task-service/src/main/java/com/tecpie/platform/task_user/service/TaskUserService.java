package com.tecpie.platform.task_user.service;

import com.tecpie.platform.task.api.resource.EngineersSumResource;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserAssignSaveCommand;
import com.tecpie.platform.task_user.api.resource.TaskUserPrintResource;
import com.tecpie.platform.task_user.api.resource.TaskUserSumTotalResource;
import com.tecpie.platform.task_user.api.resource.TaskUserTotalResource;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.mapper.TaskUserMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电任务用户表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskUserService extends IBaseService<TaskUserMapper, TaskUser> {

    /**
     * 获取详情信息
     *
     * @param id 停电通知用户id
     * @return TaskUser
     */
    TaskUser selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskUser>
     */
    List<TaskUser> searchExtensionPageList(TaskUserQueryCommand command);

    /**
     * 根据任务ID获取所有停电用户
     *
     * @param taskId 任务ID
     * @return List<TaskUser>
     */
    List<TaskUser> searchListByTaskId(Serializable taskId);

    /**
     * 查询用户最新的反馈时间
     *
     * @param taskUserIdList 停电通知用户IdList
     * @return Map<String, String>
     */
    Map<String, String> selectMaxFeedbackTime(List<Serializable> taskUserIdList);

    /**
     * 根据idList获取打印数据
     *
     * @param taskUserIdList 停电任务用户IdList
     * @return List<TaskUserPrintResource>
     */
    List<TaskUserPrintResource> getPrintData(List<Serializable> taskUserIdList);

    /**
     * 根据查询条件获取打印数据
     *
     * @param command 停电任务用户查询条件
     * @return List<TaskUserPrintResource>
     */
    List<TaskUserPrintResource> getPrintData(TaskUserQueryCommand command);

    /**
     * 批量保存
     *
     * @param task 停电任务对象
     * @param list 停电通知用户list
     */
    void saveTaskUser(Task task, List<TaskUser> list);

    /**
     * 批量更新
     *
     * @param task 停电任务对象
     * @param list 停电通知用户list
     */
    void updateTaskUser(Task task, List<TaskUser> list);

    /**
     * 批量派发停电任务用户
     *
     * @param command 派发对象
     */
    void batchAssign(TaskUserAssignSaveCommand command);

    /**
     * 一键派发，根据查询条件进行派发
     *
     * @param command 查询参数
     */
    void oneTouchAssign(TaskUserQueryCommand command);

    /**
     * 变更派发负责方
     *
     * @param command 派发对象
     */
    void changeEngineersTeam(TaskUserAssignSaveCommand command);

    /**
     * 根据查询条件一键变更派发负责方
     *
     * @param command 查询参数
     */
    void oneTouchChangeEngineersTeam(TaskUserQueryCommand command);

    /**
     * 取消派发
     *
     * @param taskUserIdList 停电通知用户idList
     * @param cancelReason   取消原因
     */
    void cancel(List<Serializable> taskUserIdList, String cancelReason);

    /**
     * 根据查询条件一键取消派发
     *
     * @param command 查询参数
     */
    void oneTouchCancel(TaskUserQueryCommand command);

    /**
     * 恢复派发，针对那些误操作取消的用户进行状态恢复
     *
     * @param id 停电通知用户id
     */
    void recoverCancel(Serializable id);

    /**
     * 根据停电任务ID物理删除停电通知用户信息
     *
     * @param taskId 任务ID
     */
    void deleteByTaskId(Serializable taskId);

    /**
     * 停电任务用户统计信息
     *
     * @param command 统计查询参数
     * @return TaskUserTotalResource
     */
    TaskUserTotalResource getTaskUserTotal(TaskUserTotalQueryCommand command);

    /**
     * 停电任务用户数据汇总信息
     *
     * @param command 统计查询参数
     * @return TaskUserSumTotalResource
     */
    TaskUserSumTotalResource getTaskUserSumTotal(TaskUserTotalQueryCommand command);

    /**
     * 生成停电通知用户回执二维码
     *
     * @param taskUser 停电任务用户对象
     * @return InputStream
     */
    InputStream genTaskQrCode(TaskUser taskUser);

    /**
     * 导入停电通知用户信息
     *
     * @param list      数据List
     * @param startTime 停电时间
     * @return Map<String, Object>
     */
    Map<String, Object> importTaskUser(List<List<Object>> list, String startTime);

    /**
     * 根据户号List查询最近一年的所有停电数据
     *
     * @param accountNumberList 户号List
     * @return List<TaskUser>
     */
    List<TaskUser> searchTaskUserList(List<String> accountNumberList);

    /**
     * 重复停电告警
     *
     * @param sourceTaskUserList 需要判断的停电任务通知用户List
     * @param taskUserDbList     数据库已有的停电任务通知用户List
     * @param task               停电任务
     * @param boolSaveAdvance    是否保存告警信息
     * @return Map<String, Integer>
     */
    Map<String, Integer> repeatPowerCutAdvance(List<TaskUser> sourceTaskUserList, List<TaskUser> taskUserDbList, Task task, boolean boolSaveAdvance);

    /**
     * 取消重复停电告警
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    @Deprecated
    void repeatPowerCutCancelAdvance(List<Serializable> taskUserIdList);

    /**
     * 用户拒签告警
     *
     * @param entity 停电任务通知用户
     * @return Map<String, Integer>
     */
    Map<String, Integer> userRejectAdvance(TaskUser entity);

    /**
     * 取消用户拒签告警
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    @Deprecated
    void userRejectCancelAdvance(List<Serializable> taskUserIdList);

    /**
     * 下达超时告警
     *
     * @param taskUserList 停电任务通知用户List
     * @return Map<String, Integer>
     */
    Map<String, Integer> releaseTimeOutAdvance(List<TaskUser> taskUserList);

    /**
     * 取消下达超时告警
     *
     * @param taskUserIdList 停电任务通知用户IdList
     */
    @Deprecated
    void releaseTimeOutCancelAdvance(List<Serializable> taskUserIdList);

    /**
     * 移动平台-工程队-首页-数据汇总
     *
     * @return {@link EngineersSumResource}
     */
    EngineersSumResource engineersSummary();

}