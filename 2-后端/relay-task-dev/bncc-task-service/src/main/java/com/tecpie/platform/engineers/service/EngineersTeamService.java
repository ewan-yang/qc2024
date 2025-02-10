package com.tecpie.platform.engineers.service;

import com.tecpie.platform.engineers.api.command.query.EngineersTeamQueryCommand;
import com.tecpie.platform.engineers.entity.EngineersTeam;
import com.tecpie.platform.engineers.mapper.EngineersTeamMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 工程队表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
public interface EngineersTeamService extends IBaseService<EngineersTeamMapper, EngineersTeam> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return EngineersTeam
     */
    EngineersTeam selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<EngineersTeam>
     */
    List<EngineersTeam> searchExtensionPageList(EngineersTeamQueryCommand command);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveEngineersTeam(EngineersTeam entity);

    /**
     * 更新
     *
     * @param id
     * @param entity
     */
    void updateEngineersTeam(Serializable id, EngineersTeam entity);

}