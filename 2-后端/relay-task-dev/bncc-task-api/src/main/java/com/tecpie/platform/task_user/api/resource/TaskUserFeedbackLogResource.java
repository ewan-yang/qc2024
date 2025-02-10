package com.tecpie.platform.task_user.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tecpie.platform.file.api.resource.CommonFileResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停电任务用户反馈表 响应结果
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Schema(description = "停电任务用户反馈表响应结果")
@Getter
@Setter
public class TaskUserFeedbackLogResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID, 自增
     */
    @Schema(description = "主键ID, 自增")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 停电任务通知用户ID
     */
    @Schema(description = "停电任务通知用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable taskUserId;

    /**
     * 停电任务通知用户类型：1-停电通知派发，2-停电通知取消
     */
    @Schema(description = "停电任务通知用户类型：1-停电通知派发，2-停电通知取消")
    private Integer feedbackType;

    /**
     * 反馈状态：310-未签，320-同意，330-拒签
     */
    @Schema(description = "反馈状态：310-未签，320-同意，330-拒签")
    private String feedbackStatus;

    /**
     * 派发方式：1-送达现场，2-微信通知，3-营销通知
     */
    @Schema(description = "派发方式：1-送达现场，2-微信通知，3-营销通知")
    private Integer deliveryMethod;

    /**
     * 新的联系方式
     */
    @Schema(description = "新的联系方式")
    private String backupPhone;

    /**
     * 拒签理由
     */
    @Schema(description = "拒签理由")
    private String rejectedReason;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 状态：0-停用，1-启用
     */
    @Schema(description = "状态：0-停用，1-启用")
    private Integer status;


    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime updateDate;

    /**
     * 文件List
     */
    @Schema(description = "文件List")
    private List<CommonFileResource> commonFileList;
}