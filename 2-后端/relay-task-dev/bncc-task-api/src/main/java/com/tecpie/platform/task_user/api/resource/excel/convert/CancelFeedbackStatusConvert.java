package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class CancelFeedbackStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 510-未签，520-同意
        if ("510".equals(value)) {
            return new WriteCellData<>("未签");
        } else if ("520".equals(value)) {
            return new WriteCellData<>("同意");
        } else {
            return new WriteCellData<>("未知状态");
        }
    }
}
