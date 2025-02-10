package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class FeedbackStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 反馈状态：310-未签，320-同意，330-拒签
        switch (value) {
            case "310":
                return new WriteCellData<>("未签");
            case "320":
                return new WriteCellData<>("同意");
            case "330":
                return new WriteCellData<>("拒签");
            default:
                return new WriteCellData<>("未知状态");
        }
    }
}
