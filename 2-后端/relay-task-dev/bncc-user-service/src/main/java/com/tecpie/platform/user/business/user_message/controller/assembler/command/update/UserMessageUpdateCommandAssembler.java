package com.tecpie.platform.user.business.user_message.controller.assembler.command.update;

import com.tecpie.platform.user.business.user_message.api.command.update.UserMessageUpdateCommand;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import com.tecpie.library.common.business.controller.assembler.IAssembler;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * 用户消息表 更新请求参数转换器
 * <p>映射方式</p>
 * <p>1、相同字段名 & 相同类型 -- 自动完成映射</p>
 * <p>2、相同字段名 & 不同类型 -- 可参考 IAssembler 中已实现的通用转换方法</p>
 * <p>3、不同字段名 -- 通过 @Mappings 注解完成映射</p>
 * <p>4、复杂映射 -- 在 afterMapping 方法中实现</p>
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMessageUpdateCommandAssembler extends IAssembler<UserMessageUpdateCommand, UserMessage> {

  UserMessageUpdateCommandAssembler INSTANCE = Mappers.getMapper(UserMessageUpdateCommandAssembler.class);

  /**
   * 映射方法
   *
   * @param command
   * @return UserMessage
   */
  @Override
  @Mappings({
    @Mapping(target = "remark", source = "remark")
  })
  UserMessage parse(UserMessageUpdateCommand command);

  @AfterMapping
  default void afterMapping(@MappingTarget UserMessage entity, UserMessageUpdateCommand command) {
    // 自定义映射

  }
}