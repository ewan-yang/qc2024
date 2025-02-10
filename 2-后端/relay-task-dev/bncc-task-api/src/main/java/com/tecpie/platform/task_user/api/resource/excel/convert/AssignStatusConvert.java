package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class AssignStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
        switch (value) {
            case "210":
                return new WriteCellData<>("未派发");
            case "220":
                return new WriteCellData<>("已派发");
            case "230":
                return new WriteCellData<>("已反馈");
            case "240":
                return new WriteCellData<>("不派发");
            default:
                return new WriteCellData<>("未知状态");
        }
    }
}
