package com.tecpie.platform.task_user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task_user.api.command.query.EngineerSummaryQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserQueryCommand;
import com.tecpie.platform.task_user.api.command.query.TaskUserTotalQueryCommand;
import com.tecpie.platform.task_user.entity.TaskUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.List;

/**
 * 停电任务用户表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Repository
public interface TaskUserMapper extends BaseMapper<TaskUser> {

    /**
     * 获取详情信息
     *
     * @param id 主键ID
     * @return TaskUser
     */
    TaskUser selectExtensionById(@Param("id") Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskUser>
     */
    List<TaskUser> searchExtensionPageList(@Param("condition") TaskUserQueryCommand command);

    /**
     * 根据户号List查询最近一年的所有停电数据
     *
     * @param accountNumberList 户号List
     * @return List<TaskUser>
     */
    List<TaskUser> searchTaskUserList(@Param("accountNumberList") List<String> accountNumberList);

    /**
     * 根据停电任务ID物理删除停电通知用户信息
     *
     * @param taskId 任务ID
     */
    void deleteByTaskId(@Param("taskId") Serializable taskId);

    /**
     * 统计停电用户派发状态、反馈状态数量
     *
     * @param command 查询参数
     * @return Integer
     */
    Integer getTaskUserTotal(@Param("condition") TaskUserTotalQueryCommand command);

    /**
     * 统计停电用户区域数量
     *
     * @param command 查询参数
     * @return Integer
     */
    Integer getTaskUserAreaTotal(@Param("condition") TaskUserTotalQueryCommand command);

    /**
     * 根据开始/结束时间+工程队id+停电任务状态检索停电用户信息
     *
     * @param condition 条件
     * @return {@link List}<{@link TaskUser}>
     */
    List<TaskUser> listByExecuteStatusAndDate(@Param("condition") EngineerSummaryQueryCommand condition);

}