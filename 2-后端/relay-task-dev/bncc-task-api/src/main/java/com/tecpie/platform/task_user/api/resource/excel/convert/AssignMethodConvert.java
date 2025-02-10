package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class AssignMethodConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 派发方式：1-送达现场，2-微信通知，3-营销通知
        switch (value) {
            case 1:
                return new WriteCellData<>("送达现场");
            case 2:
                return new WriteCellData<>("微信通知");
            case 3:
                return new WriteCellData<>("营销通知");
            default:
                return new WriteCellData<>("未知方式");
        }
    }
}
