package com.tecpie.platform.engineers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamQueryCommand;
import com.tecpie.platform.engineers.entity.EngineersTeam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 工程队表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Repository
public interface EngineersTeamMapper extends BaseMapper<EngineersTeam> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return EngineersTeam
     */
    EngineersTeam selectExtensionById(@Param("id") Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<EngineersTeam>
     */
    List<EngineersTeam> searchExtensionPageList(@Param("condition") EngineersTeamQueryCommand command);

}