package com.tecpie.platform.user.business.user_message.service.impl;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.user_message.api.command.query.UserMessageQueryCommand;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import com.tecpie.platform.user.business.user_message.mapper.UserMessageMapper;
import com.tecpie.platform.user.business.user_message.service.UserMessageService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户消息表 服务类实现
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMessageServiceImpl extends BaseServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Override
    public UserMessage selectExtensionById(Serializable id) {
        UserMessage entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("用户消息表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<UserMessage> searchExtensionPageList(UserMessageQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveUserMessage(UserMessage entity) {
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void updateUserMessage(Serializable id, UserMessage entity) {
        UserMessage existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("用户消息表[%s]不存在", id));
        }
        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void changeUserMessageStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(UserMessage::getStatus, status)
                .set(UserMessage::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(UserMessage::getUpdateDate, LocalDateTime.now())
                .eq(UserMessage::getId, id).update();

        if (!result) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("用户消息表[%s]不存在", id));
        }
    }

    @Override
    public boolean saveUserMessageBatch(List<UserMessage> userMessageList) {
        if (userMessageList.isEmpty()) {
            return false;
        }
        return this.saveBatch(userMessageList);
    }

    @Override
    public List<UserMessage> selectForUpdate() {
        return this.baseMapper.selectForUpdate();
    }

}
