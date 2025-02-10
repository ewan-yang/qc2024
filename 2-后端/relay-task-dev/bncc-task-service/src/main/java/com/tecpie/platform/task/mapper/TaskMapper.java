package com.tecpie.platform.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task.api.command.query.TaskQueryCommand;
import com.tecpie.platform.task.api.command.query.TaskTotalQueryCommand;
import com.tecpie.platform.task.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskMapper extends BaseMapper<Task> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return Task
   */
  Task selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<Task>
   */
  List<Task> searchExtensionPageList(@Param("condition") TaskQueryCommand command);

  /**
   * 查询停电通知状态数量
   *
   * @return int
   */
  int selectTaskTotal(@Param("condition") TaskTotalQueryCommand command);

    /**
     * 根据ID物理删除停电任务
     *
     * @param id
     * @return boolean
     */
    boolean deleteTask(@Param("id") Serializable id);

}