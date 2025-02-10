package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 告警类型 导出导出转换器
 * 1-下达超时告警
 * 2-下达风险告警
 * 3-用户拒签告警
 * 4-重复停电预警
 * 5-计划变更预警
 */
public class AlarmTypeConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == 1) {
            return new WriteCellData<>("下达超时告警");
        } else if (value == 2) {
            return new WriteCellData<>("下达风险告警");
        } else if (value == 3) {
            return new WriteCellData<>("用户拒签告警");
        } else if (value == 4) {
            return new WriteCellData<>("重复停电预警");
        } else if (value == 5) {
            return new WriteCellData<>("计划变更预警");
        }
        return new WriteCellData<>("其他");
    }

}
