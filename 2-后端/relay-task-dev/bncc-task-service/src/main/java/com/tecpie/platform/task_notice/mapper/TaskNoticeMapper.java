package com.tecpie.platform.task_notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task_notice.api.command.query.TaskNoticeQueryCommand;
import com.tecpie.platform.task_notice.entity.TaskNotice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电任务通知公告表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskNoticeMapper extends BaseMapper<TaskNotice> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return TaskNotice
   */
  TaskNotice selectExtensionById(@Param("id") Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<TaskNotice>
   */
  List<TaskNotice> searchExtensionPageList(@Param("condition") TaskNoticeQueryCommand command);

}