package com.tecpie.platform.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task.api.command.query.TaskExecuteStatusQueryCommand;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务执行状态表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskExecuteStatusMapper extends BaseMapper<TaskExecuteStatus> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return TaskExecuteStatus
   */
  TaskExecuteStatus selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<TaskExecuteStatus>
   */
  List<TaskExecuteStatus> searchExtensionPageList(@Param("condition") TaskExecuteStatusQueryCommand command);

    /**
     * 根据停电任务ID物理删除执行状态
     *
     * @param taskId
     */
    void deleteByTaskId(@Param("taskId") Serializable taskId);

}