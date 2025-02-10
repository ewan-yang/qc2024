package com.tecpie.platform.notice.api.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.tecpie.platform.notice.api.enums.PlanItemExecuteStatusEnum;

/**
 * 停电计划项 执行状态 导入导出转换器
 * 根据PlanItemExecuteStatusEnum枚举类进项转换
 * DZX("010", "待执行"),
 * ZXZ("020", "执行中"),
 * YWC("031", "已完成"),
 * YQX("032", "已取消"),
 * WZX("040", "未执行");
 */
public class PlanItemExecuteStatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (PlanItemExecuteStatusEnum.DZX.getCode().equals(value)) {
            return new WriteCellData<>(PlanItemExecuteStatusEnum.DZX.getMessage());
        } else if (PlanItemExecuteStatusEnum.WZX.getCode().equals(value)) {
            return new WriteCellData<>(PlanItemExecuteStatusEnum.WZX.getMessage());
        } else if (PlanItemExecuteStatusEnum.YQX.getCode().equals(value)) {
            return new WriteCellData<>(PlanItemExecuteStatusEnum.YQX.getMessage());
        } else if (PlanItemExecuteStatusEnum.YWC.getCode().equals(value)) {
            return new WriteCellData<>(PlanItemExecuteStatusEnum.YWC.getMessage());
        } else if (PlanItemExecuteStatusEnum.ZXZ.getCode().equals(value)) {
            return new WriteCellData<>(PlanItemExecuteStatusEnum.ZXZ.getMessage());
        }
        return new WriteCellData<>("其他");
    }
}
