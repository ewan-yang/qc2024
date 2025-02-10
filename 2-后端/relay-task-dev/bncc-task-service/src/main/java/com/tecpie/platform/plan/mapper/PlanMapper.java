package com.tecpie.platform.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.plan.api.command.query.PlanQueryCommand;
import com.tecpie.platform.plan.entity.Plan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 停电计划表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Repository
public interface PlanMapper extends BaseMapper<Plan> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return Plan
     */
    Plan selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<Plan>
     */
    List<Plan> searchExtensionPageList(@Param("condition") PlanQueryCommand command);

}