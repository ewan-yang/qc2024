package com.tecpie.platform.user.business.system.function.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.user.business.function.api.resource.SysOperationResource;
import com.tecpie.platform.user.business.system.function.entity.SysOperation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 操作信息表 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author tecpie
 * @since 2022-11-07
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SysOperationResourceAssembler extends IAssembler<SysOperation, SysOperationResource> {

  SysOperationResourceAssembler INSTANCE = Mappers.getMapper(SysOperationResourceAssembler.class);

  /**
   * 映射方法
   * 如有多个字段需映射，添加多个@Mapping注解
   *
   * @param entity entity
   * @return SysOperationResource
   */
  @Override
  @Mapping(target = "remark", source = "remark")
  SysOperationResource parse(SysOperation entity);

  /**
   * 自定义映射
   * @param resource resource
   * @param entity entity
   */
  @AfterMapping
  default void afterMapping(@MappingTarget SysOperationResource resource, SysOperation entity) {
    // 自定义映射

  }
}