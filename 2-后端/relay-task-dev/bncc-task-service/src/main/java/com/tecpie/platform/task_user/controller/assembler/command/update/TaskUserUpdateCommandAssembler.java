package com.tecpie.platform.task_user.controller.assembler.command.update;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.task_user.api.command.update.TaskUserUpdateCommand;
import com.tecpie.platform.task_user.entity.TaskUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 停电任务用户表 更新请求参数转换器
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
public interface TaskUserUpdateCommandAssembler extends IAssembler<TaskUserUpdateCommand, TaskUser> {

  TaskUserUpdateCommandAssembler INSTANCE = Mappers.getMapper(TaskUserUpdateCommandAssembler.class);

  /**
   * 映射方法
   *
   * @param command
   * @return TaskUser
   */
  @Override
  @Mappings({
  })
  TaskUser parse(TaskUserUpdateCommand command);

  @AfterMapping
  default void afterMapping(@MappingTarget TaskUser entity, TaskUserUpdateCommand command) {
    // 自定义映射

  }
}