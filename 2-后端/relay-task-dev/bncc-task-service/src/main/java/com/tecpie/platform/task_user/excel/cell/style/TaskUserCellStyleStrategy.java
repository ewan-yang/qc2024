package com.tecpie.platform.task_user.excel.cell.style;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskUserCellStyleStrategy extends AbstractColumnWidthStyleStrategy {

    private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Map<Integer, Integer> maxColumnWidthMap = CACHE.get(writeSheetHolder.getSheetNo());
        if (maxColumnWidthMap == null) {
            maxColumnWidthMap = new HashMap<>();
            CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
        }

        Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
        if (columnWidth >= 0) {
            if (columnWidth > 255) {
                columnWidth = 255;
            }

            Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
            if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
            }

        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (Boolean.TRUE.equals(isHead)) {
            return cell.getStringCellValue().getBytes().length + 7;
        } else {
            CellData<?> cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        return cellData.getStringValue().getBytes().length + 7;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length + 3;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length + 3;
                    default:
                        return -1;
                }
            }
        }
    }


    public static HorizontalCellStyleStrategy getStyle() {
        // 创建列头的写入样式
        WriteCellStyle headWriteCellStyle = getWriteCellStyle();
        // 创建内容的写入样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("Arial");
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 设置内容边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 设置策略：列头策略和内容策略
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    private static @NotNull WriteCellStyle getWriteCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        // 设置列头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("Aria");
        headWriteFont.setFontHeightInPoints((short) 19);
        // 加粗
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 设置列头边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        return headWriteCellStyle;
    }

}
