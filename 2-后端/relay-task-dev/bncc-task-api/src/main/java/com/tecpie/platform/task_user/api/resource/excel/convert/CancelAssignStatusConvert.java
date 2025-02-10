package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class CancelAssignStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
        switch (value) {
            case "410":
                return new WriteCellData<>("未派发");
            case "420":
                return new WriteCellData<>("已派发");
            case "430":
                return new WriteCellData<>("已反馈");
            case "440":
                return new WriteCellData<>("不派发");
            default:
                return new WriteCellData<>("未知状态");
        }
    }
}
