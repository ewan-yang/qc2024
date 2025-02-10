package com.tecpie.platform.task_notice.controller.assembler.command.save;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.task_notice.api.command.save.TaskNoticeSaveCommand;
import com.tecpie.platform.task_notice.entity.TaskNotice;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 停电任务通知公告表 保存请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskNoticeSaveCommandAssembler extends IAssembler<TaskNoticeSaveCommand, TaskNotice> {

    TaskNoticeSaveCommandAssembler INSTANCE = Mappers.getMapper(TaskNoticeSaveCommandAssembler.class);

    /**
     * 映射方法
     *
     * @param command
     * @return TaskNotice
     */
    @Override
    @Mappings({
            @Mapping(target = "remark", source = "remark")
    })
    TaskNotice parse(TaskNoticeSaveCommand command);

    @AfterMapping
    default void afterMapping(@MappingTarget TaskNotice entity, TaskNoticeSaveCommand command) {
        // 自定义映射

    }
}