package com.tecpie.platform.user.business.user_message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessageQueryCommand;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 用户消息表 数据映射接口
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Repository
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return UserMessage
     */
    UserMessage selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<UserMessage>
     */
    List<UserMessage> searchExtensionPageList(@Param("condition") UserMessageQueryCommand command);

    List<UserMessage> selectForUpdate();
}
