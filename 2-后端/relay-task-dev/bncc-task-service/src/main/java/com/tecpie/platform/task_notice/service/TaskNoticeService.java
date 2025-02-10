package com.tecpie.platform.task_notice.service;

import com.tecpie.platform.task_notice.api.command.query.TaskNoticeQueryCommand;
import com.tecpie.platform.task_notice.entity.TaskNotice;
import com.tecpie.platform.task_notice.mapper.TaskNoticeMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务通知公告表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
public interface TaskNoticeService extends IBaseService<TaskNoticeMapper, TaskNotice> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return TaskNotice
   */
  TaskNotice selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<TaskNotice>
   */
  List<TaskNotice> searchExtensionPageList(TaskNoticeQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveTaskNotice(TaskNotice entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateTaskNotice(Serializable id, TaskNotice entity);

    /**
     * 通知读取
     *
     * @param id
     */
    void read(Serializable id);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeTaskNoticeStatus(Serializable id, Integer status);

    /**
     * 发送派发通知
     * 通知类型：派发通知
     */
    void pushAssignNotice(String taskCode);

    /**
     * 发送派发通知
     * 通知类型：取消派发通知
     */
    void pushAssignCancelNotice(String taskCode);

    /**
     * 发送取消停电任务通知
     *
     * @param taskCode 任务代码
     */
    void pushTaskCancelNotice(String taskCode);

    /**
     * 发送新增停电任务通知
     *
     * @param taskCode  任务代码
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param addr      停电地区
     */
    void pushTaskSaveNotice(String taskCode, LocalDateTime startTime, LocalDateTime endTime, String addr);

    /**
     * 发送更新停电任务通知
     *
     * @param taskCode 任务代码
     */
    void pushTaskUpdateNotice(String taskCode);

}