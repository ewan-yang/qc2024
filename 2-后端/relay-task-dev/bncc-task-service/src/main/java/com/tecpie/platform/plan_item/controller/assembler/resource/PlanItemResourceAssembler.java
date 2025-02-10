package com.tecpie.platform.plan_item.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.plan_item.api.resource.PlanItemResource;
import com.tecpie.platform.plan_item.entity.PlanItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 停电计划项表 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PlanItemResourceAssembler extends IAssembler<PlanItem, PlanItemResource> {

    PlanItemResourceAssembler INSTANCE = Mappers.getMapper(PlanItemResourceAssembler.class);

    /**
     * 映射方法
     *
     * @param entity
     * @return PlanItemResource
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    PlanItemResource parse(PlanItem entity);

    @AfterMapping
    default void afterMapping(@MappingTarget PlanItemResource resource, PlanItem entity) {
        // 自定义映射

    }
}