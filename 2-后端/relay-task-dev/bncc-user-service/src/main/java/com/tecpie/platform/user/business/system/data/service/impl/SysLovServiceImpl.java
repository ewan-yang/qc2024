package com.tecpie.platform.user.business.system.data.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.data.api.command.query.SysLovQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLov;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.mapper.SysLovMapper;
import com.tecpie.platform.user.business.system.data.service.SysLovLineService;
import com.tecpie.platform.user.business.system.data.service.SysLovService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LOV定义表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLovServiceImpl extends BaseServiceImpl<SysLovMapper, SysLov> implements SysLovService {

    private static final String NON_EXIST_MESSAGE = "LOV定义表[%s]不存在";

    @Autowired
    private SysLovLineService sysLovLineService;

    /**
     * 在本类中只重载了父类的 {@link BaseServiceImpl#logicRemoveById(Serializable)}方法，重载的原因是因为逻辑删除 Lov 时，需将其所属的 LovLine 同步逻辑删除
     * 当前仅在{@link com.tecpie.platform.user.business.system.data.controller.SysLovLineRestController#deleteSysLovLineById(Serializable)}方法中使用了该方法
     * 如使用其他逻辑删除的方法，需在本类中重载相应的方法
     *
     * @param id
     * @return
     */
    @Override
    public boolean logicRemoveById(Serializable id) {
        SysLov existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        super.logicRemoveById(id);
        // 逻辑删除Lov明细数据
        return sysLovLineService.logicRemoveByIds(existEntity.getLovLineList().stream().map(SysLovLine::getId).collect(Collectors.toList()));
    }

    @Override
    public SysLov selectExtensionById(Serializable id) {
        SysLov entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        return entity;
    }

    @Override
    public List<SysLov> searchExtensionPageList(SysLovQueryCommand command) {
        List<SysLov> entityList = this.baseMapper.searchExtensionPageList(command);
        List<Serializable> idList = entityList.stream().map(SysLov::getId).collect(Collectors.toList());
        List<SysLovLine> sysLovLineList = sysLovLineService.lambdaQuery().in(SysLovLine::getLovId, idList).list();
        for (SysLov lov : entityList) {
            Serializable lovId = lov.getId();
            List<SysLovLine> collect = sysLovLineList.stream().filter(t -> lovId.equals(t.getLovId())).collect(Collectors.toList());
            lov.setLovLineList(collect);
        }
        return entityList;
    }

    @Override
    public List<SysLov> searchListByCode(String lovCode) {
        SysLovQueryCommand command = new SysLovQueryCommand();
        command.setCode(lovCode);
        return this.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveSysLov(SysLov entity) {
        this.save(entity);
        // 设置新加的Lov明细行数据的LovId
        for (SysLovLine sysLovLine : entity.getLovLineList()) {
            sysLovLine.setLovId(entity.getId());
        }
        sysLovLineService.saveBatch(entity.getLovLineList());
        return entity.getId();
    }

    @Override
    public void updateSysLov(Serializable id, SysLov entity) {
        SysLov existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }

        entity.setId(id);
        this.updateById(entity);
        sysLovLineService.cascadeUpdate(existEntity.getLovLineList(), entity.getLovLineList());
    }

    @Override
    public void changeSysLovStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(SysLov::getStatus, status)
                .set(SysLov::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(SysLov::getUpdateDate, LocalDateTime.now())
                .eq(SysLov::getId, id).update();

        if (!result) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
    }

}