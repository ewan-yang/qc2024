package com.tecpie.platform.task_user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task_user.api.command.query.TaskUserFeedbackLogQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUserFeedbackLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户反馈表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskUserFeedbackLogMapper extends BaseMapper<TaskUserFeedbackLog> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return TaskUserFeedbackLog
     */
    TaskUserFeedbackLog selectExtensionById(@Param("id") Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<TaskUserFeedbackLog>
     */
    List<TaskUserFeedbackLog> searchExtensionPageList(@Param("condition") TaskUserFeedbackLogQueryCommand command);

    /**
     * 查询用户最新的反馈时间
     *
     * @param taskUserIdList 停电通知用户IdList
     * @return List<TaskUserFeedbackLog>
     */
    List<TaskUserFeedbackLog> selectMaxFeedbackTimeList(@Param("taskUserIdList") List<Serializable> taskUserIdList);

}