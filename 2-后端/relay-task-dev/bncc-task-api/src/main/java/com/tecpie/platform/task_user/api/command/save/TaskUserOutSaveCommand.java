package com.tecpie.platform.task_user.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserOutSaveCommand {

    @Schema(description = "户号")
    private String accountNumber;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "客户地址")
    private String customerAddress;

    @Schema(description = "站线名称")
    private String stationLineName;

    @Schema(description = "联系方式")
    private String phone;

    @Schema(description = "邮编")
    private String postCode;

    @Schema(description = "用户类型：高大、低大、低非、居民、居委、物业、抄送用户、考核")
    private String userType;

    @Schema(description = "用户重要性：1-普通用户，2-重要用户")
    private String userPriority;

    @Schema(description = "是否短时停电：否，是")
    private String boolShortTime;

    @Schema(description = "所属台区")
    private String region;

    @Schema(description = "电系编号")
    private String electricalNumber;

    @Schema(description = "电压等级")
    private String voltageLevel;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "所属接入点")
    private String accessPoint;

    @Schema(description = "所属接入点名称")
    private String accessPointName;

    @Schema(description = "装接容量")
    private String capacity;

    @Schema(description = "备注")
    private String remark;

}
