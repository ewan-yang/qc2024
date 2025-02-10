package com.tecpie.platform.file.controller.assembler.command.save;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.file.api.command.save.CommonFileSaveCommand;
import com.tecpie.platform.file.entity.CommonFile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 文件和图片表 保存请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommonFileSaveCommandAssembler extends IAssembler<CommonFileSaveCommand, CommonFile> {

    CommonFileSaveCommandAssembler INSTANCE = Mappers.getMapper(CommonFileSaveCommandAssembler.class);

    /**
     * 映射方法
     *
     * @param command
     * @return CommonFile
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    CommonFile parse(CommonFileSaveCommand command);

    @AfterMapping
    default void afterMapping(@MappingTarget CommonFile entity, CommonFileSaveCommand command) {
        // 自定义映射

    }
}