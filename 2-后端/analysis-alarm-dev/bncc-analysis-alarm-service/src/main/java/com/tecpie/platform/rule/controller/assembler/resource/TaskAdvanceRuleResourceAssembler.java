package com.tecpie.platform.rule.controller.assembler.resource;

import com.tecpie.platform.rule.api.resource.TaskAdvanceRuleResource;
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
 * 告警规则维护 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskAdvanceRuleResourceAssembler extends IAssembler<TaskAdvanceRule, TaskAdvanceRuleResource> {

  TaskAdvanceRuleResourceAssembler INSTANCE = Mappers.getMapper(TaskAdvanceRuleResourceAssembler.class);

  /**
   * 映射方法
   *
   * @param entity
   * @return TaskAdvanceRuleResource
   */
  @Override
  @Mappings({
    @Mapping(target = "remark", source = "remark")
  })
  TaskAdvanceRuleResource parse(TaskAdvanceRule entity);

  @AfterMapping
  default void afterMapping(@MappingTarget TaskAdvanceRuleResource resource, TaskAdvanceRule entity) {
    // 自定义映射

  }
}