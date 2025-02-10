package com.tecpie.platform.up_record.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordQueryCommand;
import com.tecpie.platform.up_record.entity.UpdateRecord;
import com.tecpie.platform.up_record.mapper.UpdateRecordMapper;
import com.tecpie.platform.up_record.service.UpdateRecordService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 数据修改记录表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UpdateRecordServiceImpl extends BaseServiceImpl<UpdateRecordMapper, UpdateRecord> implements UpdateRecordService {

    @Override
    public UpdateRecord selectExtensionById(Serializable id) {
        UpdateRecord entity = this.baseMapper.selectExtensionById(id);
        if (entity == null){
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("数据修改记录表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<UpdateRecord> searchExtensionPageList(UpdateRecordQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveUpdateRecord(UpdateRecord entity) {
        this.save(entity);
        return entity.getId();
    }

}