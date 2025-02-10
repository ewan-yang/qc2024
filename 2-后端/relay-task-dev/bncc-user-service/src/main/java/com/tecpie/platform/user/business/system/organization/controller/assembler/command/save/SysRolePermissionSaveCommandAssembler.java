package com.tecpie.platform.user.business.system.organization.controller.assembler.command.save;

import com.tecpie.library.common.business.controller.assembler.IAssembler;
import com.tecpie.platform.user.business.organization.api.command.save.SysRolePermissionSaveCommand;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 角色与系统权限关联表 保存请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRolePermissionSaveCommandAssembler extends IAssembler<SysRolePermissionSaveCommand, SysRolePermission> {

  SysRolePermissionSaveCommandAssembler INSTANCE = Mappers.getMapper(SysRolePermissionSaveCommandAssembler.class);

  /**
   * 映射方法
   * 如有多个字段需映射，添加多个@Mapping注解
   *
   * @param command command
   * @return SysRolePermission
   */
  @Override
  @Mapping(target = "remark", source = "remark")
  SysRolePermission parse(SysRolePermissionSaveCommand command);

  /**
   * 自定义映射
   * @param entity entity
   * @param command command
   */
  @AfterMapping
  default void afterMapping(@MappingTarget SysRolePermission entity, SysRolePermissionSaveCommand command) {
    // 自定义映射

  }
}