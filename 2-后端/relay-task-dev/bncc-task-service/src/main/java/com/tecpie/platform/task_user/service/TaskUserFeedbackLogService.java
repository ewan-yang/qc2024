package com.tecpie.platform.task_user.service;

import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.api.command.save.TaskUserFeedbackSaveCommand;
import com.tecpie.platform.task_user.entity.TaskUserFeedbackLog;
import com.tecpie.platform.task_user.mapper.TaskUserFeedbackLogMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskUserFeedbackLogService extends IBaseService<TaskUserFeedbackLogMapper, TaskUserFeedbackLog> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return TaskUserFeedbackLog
     */
    TaskUserFeedbackLog selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<TaskUserFeedbackLog>
     */
    List<TaskUserFeedbackLog> searchExtensionPageList(TaskUserFeedbackLogQueryCommand command);

    /**
     * 根据停电用户ID获取反馈记录
     *
     * @param taskUserId 停电用户ID
     * @return List<TaskUserFeedbackLog>
     */
    List<TaskUserFeedbackLog> searchListByTaskUserId(Serializable taskUserId);

    /**
     * 查询用户最新的反馈时间
     *
     * @param taskUserIdList 停电通知用户IdList
     * @return List<TaskUserFeedbackLog>
     */
    List<TaskUserFeedbackLog> selectMaxFeedbackTimeList(List<Serializable> taskUserIdList);

    /**
     * 用户反馈
     *
     * @param entity
     * @return Long
     */
    Serializable saveTaskUserFeedbackLog(TaskUserFeedbackLog entity);

    /**
     * 批量上传反馈照片
     *
     * @param fileList 文件list
     */
    List<CommonFile> batchUpload(MultipartFile[] fileList);

    /**
     * 一键式反馈
     *
     * @param command 命令
     */
    void oneTouchFeedback(TaskUserFeedbackSaveCommand command);
}