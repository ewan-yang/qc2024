package com.tecpie.platform.up_record.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.up_record.api.resource.UpdateRecordResource;
import com.tecpie.platform.up_record.entity.UpdateRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 数据修改记录表 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-18
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UpdateRecordResourceAssembler extends IAssembler<UpdateRecord, UpdateRecordResource> {

    UpdateRecordResourceAssembler INSTANCE = Mappers.getMapper(UpdateRecordResourceAssembler.class);

    /**
     * 映射方法
     *
     * @param entity
     * @return UpdateRecordResource
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    UpdateRecordResource parse(UpdateRecord entity);

    @AfterMapping
    default void afterMapping(@MappingTarget UpdateRecordResource resource, UpdateRecord entity) {
        // 自定义映射

    }
}