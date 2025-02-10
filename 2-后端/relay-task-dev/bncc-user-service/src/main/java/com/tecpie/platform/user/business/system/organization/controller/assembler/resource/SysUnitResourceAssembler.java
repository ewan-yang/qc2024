package com.tecpie.platform.user.business.system.organization.controller.assembler.resource;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.user.business.organization.api.resource.SysUnitResource;
import com.tecpie.platform.user.business.system.organization.entity.SysUnit;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 组织结构表 响应结果转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SysUnitResourceAssembler extends IAssembler<SysUnit, SysUnitResource> {

  SysUnitResourceAssembler INSTANCE = Mappers.getMapper(SysUnitResourceAssembler.class);

  /**
   * 映射方法
   * 如有多个字段需映射，添加多个@Mapping注解
   *
   * @param entity entity
   * @return SysUnitResource
   */
  @Override
  @Mapping(target = "remark", source = "remark")
  SysUnitResource parse(SysUnit entity);

  /**
   * 自定义映射
   * @param resource resource
   * @param entity entity
   */
  @AfterMapping
  default void afterMapping(@MappingTarget SysUnitResource resource, SysUnit entity) {
    // 自定义映射

  }
}