package com.tecpie.platform.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.rule.api.command.query.TaskAdvanceRuleQueryCommand;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 告警规则维护 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Repository
public interface TaskAdvanceRuleMapper extends BaseMapper<TaskAdvanceRule> {

    /**
     * 获取详情信息
     *
     * @param id 主键ID
     * @return TaskAdvanceRule
     */
    TaskAdvanceRule selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command 查询参数
     * @return List<TaskAdvanceRule>
     */
    List<TaskAdvanceRule> searchExtensionPageList(@Param("condition") TaskAdvanceRuleQueryCommand command);

}