package com.tecpie.platform.task_user.api.resource;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停电任务打印数据 响应结果
 *
 * @author zhijie.tan
 * @since 2023-07-16
 */
@Getter
@Setter
@Schema(description = "停电任务打印数据响应结果")
public class TaskUserPrintResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Serializable id;

    /**
     * 外部id（配网办id）
     */
    @Schema(description = "外部id（配网办id）")
    public String outId;

    /**
     * 回执编号
     */
    @Schema(description = "回执编号")
    private String receiptCode;

    /**
     * 户号
     */
    @Schema(description = "户号")
    private String accountNumber;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称")
    private String customerName;

    /**
     * 客户地址
     */
    @Schema(description = "客户地址")
    private String customerAddress;

    /**
     * 站线名称
     */
    @Schema(description = "站线名称")
    private String stationLineName;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    private String phone;

    /**
     * 邮编
     */
    @Schema(description = "邮编")
    private String postCode;

    /**
     * 所属台区
     */
    @Schema(description = "所属台区")
    private String region;

    /**
     * 电系编号
     */
    @Schema(description = "电系编号")
    private String electricalNumber;

    /**
     * 电压等级
     */
    @Schema(description = "电压等级")
    private String voltageLevel;

    /**
     * 单位名称
     */
    @Schema(description = "单位名称")
    private String unitName;

    /**
     * 停电时间
     */
    @Schema(description = "停电时间")
    @JsonFormat(pattern = DatePattern.CHINESE_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 时/分 停电时间
     */
    @Schema(description = "时/分 停电时间")
    private String startHourTime;

    /**
     * 星期 停电时间
     */
    @Schema(description = "星期 停电时间")
    private String startWeek;

    /**
     * 送电时间
     */
    @Schema(description = "送电时间")
    @JsonFormat(pattern = DatePattern.CHINESE_DATE_PATTERN, timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 时/分 送电时间
     */
    @Schema(description = "时/分 送电时间")
    private String endHourTime;

    /**
     * 星期 送电时间
     */
    @Schema(description = "星期 送电时间")
    private String endWeek;

    /**
     * 变电站名称
     */
    @Schema(description = "变电站名称")
    private String stationName;

    /**
     * 线路名称
     */
    @Schema(description = "线路名称")
    private String lineName;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 停电范围
     */
    @Schema(description = "停电范围")
    private String ranges;

    /**
     * 停电位置
     */
    @Schema(description = "停电位置")
    private String location;

    /**
     * 工作内容
     */
    @Schema(description = "工作内容")
    private String jobContent;

    /**
     * 新的联系方式
     */
    @Schema(description = "新的联系方式")
    private String backupPhone;

    /**
     * 停电原因
     */
    @Schema(description = "停电原因")
    private String reason;

    @Schema(description = "二维码")
    private String qrCode;

}