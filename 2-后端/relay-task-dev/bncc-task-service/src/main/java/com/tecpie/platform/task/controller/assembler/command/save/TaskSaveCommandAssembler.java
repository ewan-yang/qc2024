package com.tecpie.platform.task.controller.assembler.command.save;

import com.tecpie.platform.task.api.command.save.TaskSaveCommand;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.library.common.business.controller.assembler.IAssembler;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 停电任务表 保存请求参数转换器
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
public interface TaskSaveCommandAssembler extends IAssembler<TaskSaveCommand, Task> {

  TaskSaveCommandAssembler INSTANCE = Mappers.getMapper(TaskSaveCommandAssembler.class);

  /**
   * 映射方法
   *
   * @param command
   * @return Task
   */
  @Override
  @Mappings({
    @Mapping(target = "remark", source = "remark")
  })
  Task parse(TaskSaveCommand command);

  @AfterMapping
  default void afterMapping(@MappingTarget Task entity, TaskSaveCommand command) {
    // 自定义映射

  }
}