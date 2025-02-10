package com.tecpie.platform.seq.controller.assembler.command.save;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.seq.api.command.save.SeqGenSaveCommand;
import com.tecpie.platform.seq.entity.SeqGen;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 序列表 保存请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeqGenSaveCommandAssembler extends IAssembler<SeqGenSaveCommand, SeqGen> {

    SeqGenSaveCommandAssembler INSTANCE = Mappers.getMapper(SeqGenSaveCommandAssembler.class);

    /**
     * 映射方法
     *
     * @param command
     * @return SeqGen
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    SeqGen parse(SeqGenSaveCommand command);

    @AfterMapping
    default void afterMapping(@MappingTarget SeqGen entity, SeqGenSaveCommand command) {
        // 自定义映射

    }
}