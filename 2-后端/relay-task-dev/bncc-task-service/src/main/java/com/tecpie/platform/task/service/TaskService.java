package com.tecpie.platform.task.service;

import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskTotalQueryCommand;
import com.tecpie.platform.task.api.command.save.TaskOutSaveCommand;
import com.tecpie.platform.task.api.command.update.TaskCancelCommand;
import com.tecpie.platform.task.api.resource.TaskSumTotalResource;
import com.tecpie.platform.task.api.resource.TaskTotalResource;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.mapper.TaskMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电任务表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskService extends IBaseService<TaskMapper, Task> {

    /**
     * 获取详情信息
     *
     * @param id 停电任务ID
     * @return Task
     */
    Task selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<Task>
     */
    List<Task> searchExtensionPageList(TaskQueryCommand command);

    /**
     * 根据任务ID查询相关版本数据，并按照版本排序
     *
     * @param id 停电任务ID
     * @return List<Task>
     */
    List<Task> searchVersionList(Serializable id);

    /**
     * 保存
     *
     * @param entity 数据对象
     * @return Serializable
     */
    Serializable saveTask(Task entity);

    /**
     * 更新
     *
     * @param id     停电任务ID
     * @param entity 数据对象
     * @return Serializable
     */
    Serializable updateTask(Serializable id, Task entity);

    /**
     * 验证用户数据是否有重复停电
     *
     * @param entity 数据对象
     * @return Map<String, Object>
     */
    Map<String, Object> validRepeatPowerCut(Task entity);

    /**
     * 批量变更停电任务执行状态
     *
     * @param idList        停电任务IdList
     * @param executeStatus 执行状态
     */
    void changeTaskExecuteStatus(List<Serializable> idList, String executeStatus);

    /**
     * 变更停电任务执行状态
     *
     * @param id            停电任务ID
     * @param executeStatus 执行状态
     */
    void changeTaskExecuteStatus(Serializable id, String executeStatus);

    /**
     * 取消停电任务
     *
     * @param taskCancelCommand 取消停电任务参数
     */
    void cancelTask(TaskCancelCommand taskCancelCommand);

    /**
     * 取消停电任务
     *
     * @param taskCancelCommand 取消停电任务参数
     */
    void revokeCancelTask(TaskCancelCommand taskCancelCommand);

    /**
     * 批量上传附件
     *
     * @param fileList 文件list
     */
    List<CommonFile> batchUpload(MultipartFile[] fileList);

    /**
     * 删除停电任务
     *
     * @param taskIdList taskIdList
     */
    void deleteTask(List<Serializable> taskIdList);

    /**
     * 停电任务统计信息
     *
     * @param command 统计查询参数
     * @return TaskTotalResource
     */
    TaskTotalResource getTaskTotal(TaskTotalQueryCommand command);

    /**
     * 停电任务数据汇总
     *
     * @param command 统计查询参数
     * @return TaskSumTotalResource
     */
    TaskSumTotalResource getTaskSumTotal(TaskTotalQueryCommand command);

    /**
     * 运方下达风险预警
     *
     * @param entity 停电任务
     * @return Map<String, Integer>
     */
    Map<String, Integer> carrierReleaseAdvance(Task entity);

    /**
     * 导出数据
     *
     * @param dataList 待导出的数据
     * @param response HttpServletResponse
     */
    void download(List<Task> dataList, HttpServletResponse response) throws IOException;


    /**
     * 插入或保存批处理
     *
     * @param taskOutSaveCommandList 任务输出保存命令列表
     */
    void insertOrSaveBatch(List<TaskOutSaveCommand> taskOutSaveCommandList);
}