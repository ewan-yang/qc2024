package com.tecpie.platform.user.business.user_message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户消息表 实体类
 *
 * @author tecpie
 * @since 2022-08-19
 */
@Getter
@Setter
@TableName("user_message")
public class UserMessage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 接收消息用户ID
     */
    @TableField(value = "user_id")
    private Serializable userId;

    /**
     * 消息跳转目标ID
     */
    @TableField(value = "entity_id")
    private Serializable entityId;

    /**
     * 是否已推送
     */
    private Boolean sent;
}
