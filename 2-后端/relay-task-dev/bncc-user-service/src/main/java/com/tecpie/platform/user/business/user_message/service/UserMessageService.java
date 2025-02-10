package com.tecpie.platform.user.business.user_message.service;

import com.tecpie.platform.user.business.user_message.api.command.query.UserMessageQueryCommand;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import com.tecpie.platform.user.business.user_message.mapper.UserMessageMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户消息表 服务类接口
 *
 * @author tecpie
 * @since 2022-08-19
 */
public interface UserMessageService extends IBaseService<UserMessageMapper, UserMessage> {

    /**
     * 获取详情信息
     *
     * @param id id
     * @return UserMessage
     */
    UserMessage selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command 查看參數
     * @return List<UserMessage>
     */
    List<UserMessage> searchExtensionPageList(UserMessageQueryCommand command);

    /**
     * 保存
     *
     * @param entity 数据对象
     * @return Serializable
     */
    Serializable saveUserMessage(UserMessage entity);

    /**
     * 更新
     *
     * @param id     主鍵ID
     * @param entity 数据对象
     */
    void updateUserMessage(Serializable id, UserMessage entity);

    /**
     * 变更状态
     *
     * @param id     主鍵ID
     * @param status 状态
     */
    void changeUserMessageStatus(Serializable id, Integer status);

    boolean saveUserMessageBatch(List<UserMessage> parseList);

    List<UserMessage> selectForUpdate();
}
