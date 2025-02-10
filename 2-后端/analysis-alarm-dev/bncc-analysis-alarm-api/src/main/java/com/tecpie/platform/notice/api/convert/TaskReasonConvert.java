package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 停电通知-停电原因 导出导出转换器
 */
public class TaskReasonConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == 1) {
            return new WriteCellData<>("设备检索");
        } else if (value == 2) {
            return new WriteCellData<>("配合客户接入");
        } else if (value == 3) {
            return new WriteCellData<>("配合市政过程");
        }
        return new WriteCellData<>("其他");
    }

}
