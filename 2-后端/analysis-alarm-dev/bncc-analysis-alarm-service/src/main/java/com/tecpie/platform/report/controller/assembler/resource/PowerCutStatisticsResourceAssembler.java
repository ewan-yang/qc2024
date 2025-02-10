package com.tecpie.platform.report.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.report.api.resource.PowerCutStatisticsResource;
import com.tecpie.platform.report.entity.PowerCutStatistics;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 停电统计 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PowerCutStatisticsResourceAssembler extends IAssembler<PowerCutStatistics, PowerCutStatisticsResource> {

    PowerCutStatisticsResourceAssembler INSTANCE = Mappers.getMapper(PowerCutStatisticsResourceAssembler.class);

    /**
     * 映射方法
     *
     * @param entity
     * @return PowerCutStatisticsResource
     */
    @Override
    @Mappings({
    })
    PowerCutStatisticsResource parse(PowerCutStatistics entity);

    @AfterMapping
    default void afterMapping(@MappingTarget PowerCutStatisticsResource resource, PowerCutStatistics entity) {
        // 自定义映射

    }
}