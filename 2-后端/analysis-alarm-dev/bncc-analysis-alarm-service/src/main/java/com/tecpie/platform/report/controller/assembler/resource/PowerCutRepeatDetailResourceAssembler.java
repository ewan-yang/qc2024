package com.tecpie.platform.report.controller.assembler.resource;


import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.report.api.resource.PowerCutRepeatDetailResource;
import com.tecpie.platform.report.entity.PowerCutRepeatDetail;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PowerCutRepeatDetailResourceAssembler extends IAssembler<PowerCutRepeatDetail, PowerCutRepeatDetailResource> {

    PowerCutRepeatDetailResourceAssembler INSTANCE = Mappers.getMapper(PowerCutRepeatDetailResourceAssembler.class);

    /**
     * 映射方法
     *
     * @param entity
     * @return PowerCutRepeatStatisticsResource
     */
    @Override
    @Mappings({
    })
    PowerCutRepeatDetailResource parse(PowerCutRepeatDetail entity);

    @AfterMapping
    default void afterMapping(@MappingTarget PowerCutRepeatDetailResource resource, PowerCutRepeatDetail entity) {
        // 自定义映射

    }

}
