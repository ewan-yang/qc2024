package com.tecpie.platform.file.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.file.api.resource.CommonFileResource;
import com.tecpie.platform.file.entity.CommonFile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 文件和图片表 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommonFileResourceAssembler extends IAssembler<CommonFile, CommonFileResource> {

    CommonFileResourceAssembler INSTANCE = Mappers.getMapper(CommonFileResourceAssembler.class);

    /**
     * 映射方法
     *
     * @param entity
     * @return CommonFileResource
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    CommonFileResource parse(CommonFile entity);

    @AfterMapping
    default void afterMapping(@MappingTarget CommonFileResource resource, CommonFile entity) {
        // 自定义映射

    }
}