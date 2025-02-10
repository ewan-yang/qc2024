package com.tecpie.platform.user.security.igw.schedule;

import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.business.user_message.entity.UserMessage;
import com.tecpie.platform.user.business.user_message.service.UserMessageService;
import com.tecpie.platform.user.security.igw.service.IgwService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据传输 服务类实现
 *
 * @author tecpie
 * @since 2022-07-29
 */
@Slf4j
@ConditionalOnProperty(prefix = "igw", name = "messageAppId")
@Component
@Transactional(rollbackFor = Exception.class)
public class MessageSchedule {

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private IgwService igwService;

    @Autowired
    private SysUserService userService;

    @Scheduled(cron = "0 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage() {
        List<UserMessage> messageList = userMessageService.selectForUpdate();
        if (!messageList.isEmpty()) {
            Map<Serializable, String> userMap = userService.lambdaQuery()
                    .in(BaseEntity::getId, messageList.stream().map(UserMessage::getUserId).collect(
                            Collectors.toList())).select(SysUser::getId, SysUser::getCode).list().stream()
                    .collect(Collectors.toMap(BaseEntity::getId, SysUser::getCode));
            messageList.stream()
                    .collect(Collectors.groupingBy(BaseEntity::getRemark)).forEach((content, messages) -> {
                List<String> userCodes = new ArrayList<>();
                messages.forEach(message -> {
                    userCodes.add(userMap.get(message.getUserId()));
                    message.setSent(Boolean.TRUE);
                });
                igwService.sendMsg2Users(userCodes, content);
            });
            userMessageService.updateBatchById(messageList);
        }
    }
}
