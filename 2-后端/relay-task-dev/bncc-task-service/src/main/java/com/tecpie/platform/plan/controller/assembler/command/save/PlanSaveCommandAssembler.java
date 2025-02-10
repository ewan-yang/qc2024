package com.tecpie.platform.plan.controller.assembler.command.save;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.plan.api.command.save.PlanSaveCommand;
import com.tecpie.platform.plan.entity.Plan;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 停电计划表 保存请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-12
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanSaveCommandAssembler extends IAssembler<PlanSaveCommand, Plan> {

    PlanSaveCommandAssembler INSTANCE = Mappers.getMapper(PlanSaveCommandAssembler.class);

    /**
     * 映射方法
     *
     * @param command
     * @return Plan
     */
    @Override
    @Mappings({
    })
    Plan parse(PlanSaveCommand command);

    @AfterMapping
    default void afterMapping(@MappingTarget Plan entity, PlanSaveCommand command) {
        // 自定义映射

    }
}