package com.tecpie.platform.up_record.service;

import com.tecpie.platform.up_record.api.command.query.UpdateRecordQueryCommand;
import com.tecpie.platform.up_record.entity.UpdateRecord;
import com.tecpie.platform.up_record.mapper.UpdateRecordMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 数据修改记录表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
public interface UpdateRecordService extends IBaseService<UpdateRecordMapper, UpdateRecord> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return UpdateRecord
     */
    UpdateRecord selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<UpdateRecord>
     */
    List<UpdateRecord> searchExtensionPageList(UpdateRecordQueryCommand command);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveUpdateRecord(UpdateRecord entity);

}