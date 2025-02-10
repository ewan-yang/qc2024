package com.tecpie.platform.seq.service;

import com.tecpie.platform.seq.api.command.query.SeqGenQueryCommand;
import com.tecpie.platform.seq.entity.SeqGen;
import com.tecpie.platform.seq.mapper.SeqGenMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 序列表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
public interface SeqGenService extends IBaseService<SeqGenMapper, SeqGen> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return SeqGen
     */
    SeqGen selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<SeqGen>
     */
    List<SeqGen> searchExtensionPageList(SeqGenQueryCommand command);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveSeqGen(SeqGen entity);

    /**
     * 更新
     *
     * @param id
     * @param entity
     */
    void updateSeqGen(Serializable id, SeqGen entity);

    /**
     * 变更状态
     *
     * @param id
     * @param status
     */
    void changeSeqGenStatus(Serializable id, Integer status);

    /**
     * 根据业务类型获取对应的序列值
     *
     * @param businessType 业务类型
     * @return SeqDto
     */
    SeqGen getSeqByBusinessType(String businessType);

    /**
     * 根据业务类型获取对应的序列值
     *
     * @param businessType 业务类型
     * @return String
     */
    String getValue(String businessType);

    /**
     * 根据序列对象获取新的序列值
     *
     * @param seqGen 序列对象
     * @return String
     */
    String getValue(SeqGen seqGen);
}