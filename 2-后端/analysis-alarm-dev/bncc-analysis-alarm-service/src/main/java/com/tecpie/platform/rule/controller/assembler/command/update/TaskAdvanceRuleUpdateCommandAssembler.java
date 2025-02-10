package com.tecpie.platform.rule.controller.assembler.command.update;

import com.tecpie.platform.rule.api.command.update.TaskAdvanceRuleUpdateCommand;
import com.tecpie.platform.rule.entity.TaskAdvanceRule;
import com.tecpie.library.common.business.controller.assembler.IAssembler;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 告警规则维护 更新请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskAdvanceRuleUpdateCommandAssembler extends IAssembler<TaskAdvanceRuleUpdateCommand, TaskAdvanceRule> {

  TaskAdvanceRuleUpdateCommandAssembler INSTANCE = Mappers.getMapper(TaskAdvanceRuleUpdateCommandAssembler.class);

  /**
   * 映射方法
   *
   * @param command
   * @return TaskAdvanceRule
   */
  @Override
  @Mappings({
    @Mapping(target = "remark", source = "remark")
  })
  TaskAdvanceRule parse(TaskAdvanceRuleUpdateCommand command);

  @AfterMapping
  default void afterMapping(@MappingTarget TaskAdvanceRule entity, TaskAdvanceRuleUpdateCommand command) {
    // 自定义映射

  }
}