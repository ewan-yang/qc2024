package com.tecpie.platform.up_record.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.up_record.api.command.query.UpdateRecordQueryCommand;
import com.tecpie.platform.up_record.entity.UpdateRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 数据修改记录表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Repository
public interface UpdateRecordMapper extends BaseMapper<UpdateRecord> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return UpdateRecord
     */
    UpdateRecord selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<UpdateRecord>
     */
    List<UpdateRecord> searchExtensionPageList(@Param("condition") UpdateRecordQueryCommand command);

}