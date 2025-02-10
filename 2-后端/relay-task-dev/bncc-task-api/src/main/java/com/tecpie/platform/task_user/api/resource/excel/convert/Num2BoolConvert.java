package com.tecpie.platform.task_user.api.resource.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class Num2BoolConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value.equals(1)) {
            return new WriteCellData<>("是");
        } else {
            return new WriteCellData<>("否");
        }
    }

}
