package com.tecpie.platform.task_user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task_user.api.command.query.TaskUserFollowQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUserFollow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户跟进情况表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskUserFollowMapper extends BaseMapper<TaskUserFollow> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return TaskUserFollow
   */
  TaskUserFollow selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<TaskUserFollow>
   */
  List<TaskUserFollow> searchExtensionPageList(@Param("condition") TaskUserFollowQueryCommand command);

}