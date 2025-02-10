package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 用户类型 导入导出转换器
 * 1-高大
 * 2-低大
 * 3-低非
 * 4-居民
 * 5-居委
 * 6-物业
 * 7-抄送用户
 * 8-考核
 */
public class UserTypeConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        switch (value) {
            case 1:
                return new WriteCellData<>("高大");
            case 2:
                return new WriteCellData<>("低大");
            case 3:
                return new WriteCellData<>("低非");
            case 4:
                return new WriteCellData<>("居民");
            case 5:
                return new WriteCellData<>("居委");
            case 6:
                return new WriteCellData<>("物业");
            case 7:
                return new WriteCellData<>("抄送用户");
            case 8:
                return new WriteCellData<>("考核");
            default:
                return new WriteCellData<>("其他");
        }
    }

}
