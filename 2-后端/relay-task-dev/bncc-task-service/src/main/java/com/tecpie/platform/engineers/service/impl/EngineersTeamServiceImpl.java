package com.tecpie.platform.engineers.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.engineers.api.command.query.EngineersTeamQueryCommand;
import com.tecpie.platform.engineers.entity.EngineersTeam;
import com.tecpie.platform.engineers.mapper.EngineersTeamMapper;
import com.tecpie.platform.engineers.service.EngineersTeamService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 工程队表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EngineersTeamServiceImpl extends BaseServiceImpl<EngineersTeamMapper, EngineersTeam> implements EngineersTeamService {

    @Override
    public EngineersTeam selectExtensionById(Serializable id) {
        EngineersTeam entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("工程队表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<EngineersTeam> searchExtensionPageList(EngineersTeamQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveEngineersTeam(EngineersTeam entity) {
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updateEngineersTeam(Serializable id, EngineersTeam entity) {
        EngineersTeam existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("工程队表[%s]不存在", id));
        }
        entity.setId(id);
        this.updateById(entity);
    }

}