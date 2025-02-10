package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.tecpie.platform.notice.api.enums.TaskExecuteStatusEnum;

/**
 * 停电通知 执行状态 导出导出转换器
 * 通过枚举类实现
 * DTJ("110", "待提交"),
 * DPF("120", "待派发"),
 * ZXZ("130", "执行中"),
 * YFK("140", "已反馈"),
 * YQX("150", "已取消"),
 * YWC("161", "已完成");
 */
public class TaskExecuteStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) throws Exception {
        if (TaskExecuteStatusEnum.DPF.getCode().equals(value)){
            return new WriteCellData<>(TaskExecuteStatusEnum.DPF.getMessage());
        } else if (TaskExecuteStatusEnum.DTJ.getCode().equals(value)) {
            return new WriteCellData<>(TaskExecuteStatusEnum.DTJ.getMessage());
        } else if (TaskExecuteStatusEnum.YFK.getCode().equals(value)) {
            return new WriteCellData<>(TaskExecuteStatusEnum.YFK.getMessage());
        } else if (TaskExecuteStatusEnum.YQX.getCode().equals(value)) {
            return new WriteCellData<>(TaskExecuteStatusEnum.YQX.getMessage());
        } else if (TaskExecuteStatusEnum.YWC.getCode().equals(value)) {
            return new WriteCellData<>(TaskExecuteStatusEnum.YWC.getMessage());
        } else if (TaskExecuteStatusEnum.ZXZ.getCode().equals(value)) {
            return new WriteCellData<>(TaskExecuteStatusEnum.ZXZ.getMessage());
        } else {
            return new WriteCellData<>("其他");
        }
    }

}
