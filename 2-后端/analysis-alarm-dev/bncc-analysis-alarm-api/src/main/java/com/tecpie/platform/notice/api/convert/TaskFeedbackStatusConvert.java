package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.tecpie.platform.notice.api.enums.TaskUserFeedbackStatusEnum;

/**
 * 停电用户-用户反馈情况 导入导出转换器
 * 根据反馈状况枚举类确定
 * WQ("310", "未签"),
 * TY("320", "同意"),
 * JQ("330", "拒签");
 */
public class TaskFeedbackStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (TaskUserFeedbackStatusEnum.WQ.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserFeedbackStatusEnum.WQ.getMessage());
        } else if (TaskUserFeedbackStatusEnum.TY.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserFeedbackStatusEnum.TY.getMessage());
        } else if (TaskUserFeedbackStatusEnum.JQ.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserFeedbackStatusEnum.JQ.getMessage());
        }
        return new WriteCellData<>("其他");
    }
}
