package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.tecpie.platform.notice.api.enums.TaskUserAssignStatusEnum;

/**
 * 停电通知-派发状态 导入导出转换器
 * 根据派发状态枚举类确定状态
 * WPF("210", "未派发"),
 * YPF("220", "已派发"),
 * YFK("230", "已反馈"),
 * BPF("240", "不派发");
 */
public class TaskAssignStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (TaskUserAssignStatusEnum.BPF.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserAssignStatusEnum.BPF.getMessage());
        } else if (TaskUserAssignStatusEnum.WPF.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserAssignStatusEnum.WPF.getCode());
        } else if (TaskUserAssignStatusEnum.YPF.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserAssignStatusEnum.YPF.getMessage());
        } else if (TaskUserAssignStatusEnum.YFK.getCode().equals(value)) {
            return new WriteCellData<>(TaskUserAssignStatusEnum.YFK.getMessage());
        }
        return new WriteCellData<>("其他");
    }

}
