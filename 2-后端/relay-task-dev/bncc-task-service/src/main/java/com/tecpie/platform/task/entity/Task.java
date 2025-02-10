package com.tecpie.platform.task.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.task_user.entity.TaskUser;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 停电任务表 实体类
 *
 * @author zhijie.tan
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_task")
public class Task extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 外部id
     */
    @TableField("out_id")
    private String outId;

    /**
     * 停电计划项ID
     */
    @TableField(value = "plan_item_id")
    private Serializable planItemId;

    /**
     * 任务编号
     */
    @TableField(value = "task_code")
    private String taskCode;

    /**
     * 关联的任务编号
     */
    @TableField(value = "ass_task_code")
    private String assTaskCode;

    /**
     * 单位名称
     */
    @TableField(value = "unit_name")
    private String unitName;

    /**
     * 停电类型：1-计划停电
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 停电原因：1-设备检修，2-配合客户接入，3-配合市政过程
     */
    @TableField(value = "reason")
    private Integer reason;

    /**
     * 停电时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 送电时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 变电站名称
     */
    @TableField(value = "station_name")
    private String stationName;

    /**
     * 线路名称
     */
    @TableField(value = "line_name")
    private String lineName;

    /**
     * 设备名称
     */
    @TableField(value = "device_name")
    private String deviceName;

    /**
     * 停电范围
     */
    @TableField(value = "ranges")
    private String ranges;

    /**
     * 停电位置
     */
    @TableField(value = "location")
    private String location;

    /**
     * 工作内容
     */
    @TableField(value = "job_content")
    private String jobContent;

    /**
     * 是否通知媒体：0-否，1-是
     */
    @TableField(value = "bool_notify_media")
    private Integer boolNotifyMedia;

    /**
     * 任务派发用户Id
     */
    @TableField(value = "assigned_by")
    private Long assignedBy;

    /**
     * 任务派发用户姓名
     */
    @TableField(exist = false)
    private String assignedByName;

    /**
     * 停电任务通知来源
     */
    @TableField(value = "task_source")
    private Long taskSource;

    /**
     * 取消原因
     */
    @TableField(value = "cancel_reason")
    private String cancelReason;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 未派发数量
     */
    @TableField(value = "unassigned_qty")
    private Integer unassignedQty;

    /**
     * 已派发数量
     */
    @TableField(value = "assigned_qty")
    private Integer assignedQty;

    /**
     * 已取消数量
     */
    @TableField(value = "cancelled_qty")
    private Integer cancelledQty;

    /**
     * 拒签数量
     */
    @TableField(value = "rejected_qty")
    private Integer rejectedQty;

    /**
     * 同意数量
     */
    @TableField(value = "agreed_qty")
    private Integer agreedQty;

    /**
     * 未签数量
     */
    @TableField(value = "unsigned_qty")
    private Integer unsignedQty;

    /**
     * 取消通知未派发数量
     */
    @TableField(value = "cancel_unassigned_qty")
    private Integer cancelUnassignedQty;

    /**
     * 取消通知已派发数量
     */
    @TableField(value = "cancel_assigned_qty")
    private Integer cancelAssignedQty;

    /**
     * 取消通知未签数量
     */
    @TableField(value = "cancel_unsigned_qty")
    private Integer cancelUnsignedQty;

    /**
     * 取消通知同意数量
     */
    @TableField(value = "cancel_agreed_qty")
    private Integer cancelAgreedQty;

    /**
     * 执行状态Id
     */
    @TableField(value = "execute_status_id")
    private Serializable executeStatusId;

    /**
     * 告警状态
     */
    @TableField(value = "advance_status")
    private Integer advanceStatus;

    /**
     * 版本
     */
    @TableField(value = "version")
    private Long version;

    /**
     * 扩展属性1
     */
    @TableField(value = "attribute1")
    private String attribute1;

    /**
     * 扩展属性2
     */
    @TableField(value = "attribute2")
    private String attribute2;

    /**
     * 扩展属性3
     */
    @TableField(value = "attribute3")
    private String attribute3;

    /**
     * 扩展属性4
     */
    @TableField(value = "attribute4")
    private String attribute4;

    /**
     * 确认时间
     */
    @TableField(value = "confirm_time")
    private LocalDateTime confirmTime;

    /**
     * 工程队Ids
     */
    @TableField(value = "engineers_team_ids")
    private String engineersTeamIds;

    /**
     * 停电任务通知用户List
     */
    @TableField(exist = false)
    private List<TaskUser> taskUserList;

    /**
     * 提交类型
     */
    @TableField(exist = false)
    private Integer submitType;

    /**
     * 执行状态对象
     */
    @TableField(exist = false)
    private TaskExecuteStatus taskExecuteStatus;

    /**
     * 文件List
     */
    @TableField(exist = false)
    private List<CommonFile> commonFileList;

    /**
     * 关联的工程名称名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 判断是否需要重复性校验 0-未校验重复性 1-已校验重复性 2-单条查询不再显示
     */
    @TableField(value = "is_repeat")
    private Integer isRepeat;

    public void copy(Task source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

    // 自定义的Comparator用于Serializable类型
    public static class SerializableComparator implements Comparator<Serializable> {
        @Override
        public int compare(Serializable o1, Serializable o2) {
            // 实现比较逻辑，可能需要根据具体情况进行处理
            // 这里简单比较toString()的结果
            return o1.toString().compareTo(o2.toString());
        }
    }
}
