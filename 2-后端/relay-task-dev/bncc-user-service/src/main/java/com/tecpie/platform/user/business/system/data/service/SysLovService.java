package com.tecpie.platform.user.business.system.data.service;

import com.tecpie.platform.user.business.data.api.command.query.SysLovQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLov;
import com.tecpie.platform.user.business.system.data.mapper.SysLovMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * LOV定义表 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysLovService extends IBaseService<SysLovMapper, SysLov> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return SysLov
     */
    SysLov selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<SysLov>
     */
    List<SysLov> searchExtensionPageList(SysLovQueryCommand command);

    /**
     * 根据code获取字典数据
     *
     * @param lovCode 字典Key
     * @return List<SysLov>
     */
    List<SysLov> searchListByCode(String lovCode);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveSysLov(SysLov entity);

    /**
     * 更新
     *
     * @param id
     * @param entity
     */
    void updateSysLov(Serializable id, SysLov entity);

    /**
     * 变更状态
     *
     * @param id
     * @param status
     */
    void changeSysLovStatus(Serializable id, Integer status);

}