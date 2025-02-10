package com.tecpie.platform.seq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.seq.api.command.query.SeqGenQueryCommand;
import com.tecpie.platform.seq.entity.SeqGen;
import com.tecpie.platform.seq.gen.SequenceGenerateEngine;
import com.tecpie.platform.seq.mapper.SeqGenMapper;
import com.tecpie.platform.seq.service.SeqGenService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 序列表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SeqGenServiceImpl extends BaseServiceImpl<SeqGenMapper, SeqGen> implements SeqGenService {

    @Override
    public SeqGen selectExtensionById(Serializable id) {
        SeqGen entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("序列表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<SeqGen> searchExtensionPageList(SeqGenQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveSeqGen(SeqGen entity) {
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updateSeqGen(Serializable id, SeqGen entity) {
        SeqGen existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("序列表[%s]不存在", id));
        }

        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void changeSeqGenStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(SeqGen::getStatus, status)
                .set(SeqGen::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(SeqGen::getUpdateDate, LocalDateTime.now())
                .eq(SeqGen::getId, id).update();

        if (!result) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("序列表[%s]不存在", id));
        }
    }

    @Override
    public SeqGen getSeqByBusinessType(String businessType) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SeqGen>().eq(SeqGen::getBusinessType, businessType));
    }

    @Override
    public synchronized String getValue(String businessType) {
        // 根据业务类型获取序列数据
        SeqGen seqGen = this.getSeqByBusinessType(businessType);
        if (seqGen == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("序列businessType[%s]不存在", businessType));
        }
        return this.getValue(seqGen);
    }

    @Override
    public synchronized String getValue(SeqGen seqGen) {
        // 新的序列值 = 编号 + 步长
        int newCode = seqGen.getCode() + seqGen.getStep();
        seqGen.setCode(newCode);
        // 更新序列
        this.updateById(seqGen);
        // 返回新序列值
        return this.genSeq(newCode, seqGen.getFormatStr());
    }

    /**
     * 生成序列
     */
    private String genSeq(int newNumber, String formatStr) {
        Map<String, String> parameterMap = Maps.newHashMap();
        parameterMap.put("currentNum", String.valueOf(newNumber));
        SequenceGenerateEngine sequenceGenerateEngine = SequenceGenerateEngine.getInstance();
        sequenceGenerateEngine.setFormatStr(formatStr);
        return sequenceGenerateEngine.generate(parameterMap);
    }


}