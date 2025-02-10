package com.tecpie.platform.notice.api.command.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 告警通知 保存请求参数
 *
 * @author zhijie.tan
 * @since 2023-07-19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警通知 保存请求参数")
public class AdvanceNoticeSaveCommand {

    /**
     * 业务List
     */
    @Schema(description = "业务List")
    private List<AdvanceParamCommand> businessList;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)")
    private Integer days;

    /**
     * 业务类型
     */
    @Schema(description = "业务类型")
    private Integer businessType;

    public List<Serializable> getBusinessIdList() {
        if (CollectionUtils.isEmpty(this.businessList)) {
            return null;
        }
        return this.businessList.stream().map(e -> e.getBusinessId()).collect(Collectors.toList());
    }
}
