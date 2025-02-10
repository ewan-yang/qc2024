package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 停电通知-停电类型 导出导出转换器
 */
public class TaskTypeConvert implements Converter<Integer> {


    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == 1){
            return new WriteCellData<>("计划停电");
        }
        return new WriteCellData<>("其他");
    }

}
