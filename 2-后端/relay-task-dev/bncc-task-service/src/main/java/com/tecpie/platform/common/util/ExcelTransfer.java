package com.tecpie.platform.common.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.platform.common.enums.ExcelVO;
import com.tecpie.platform.task_user.excel.cell.style.TaskUserCellStyleStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Data
@Component
public class ExcelTransfer<T> {

    //读取实体类的包名地址+1
    private static final String PACKAGE_NAME = "com.example.";

    //读取service实现类地址长度+1
    private int size = 25;

    /**
     * 下载excel
     *
     * @param response http
     * @param list     需要下载的数据
     * @param name     文件名称
     * @param sheet    表名
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String name, String sheet,
                            IService<T> service) throws ClassNotFoundException {
        String className = service.getClass().getName();
        String s = className.substring(size, className.length() - 11);
        Class<?> aClass = Class.forName(PACKAGE_NAME + s);
        export(response, list, name, sheet, null, null, aClass);
    }

    /**
     * 下载excel
     *
     * @param response http
     * @param list     需要下载的数据
     * @param name     文件名称
     * @param sheet    表名
     * @param aClass   实体类
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String name, String sheet, Class<?> aClass) {
        export(response, list, name, sheet, null, null, aClass);
    }

    /**
     * 下载excel
     *
     * @param response      http
     * @param list          需要下载的数据
     * @param name          文件名称
     * @param sheet         表名
     * @param fieldNameList 字段名称列表
     * @param aClass        实体类
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String name, String sheet, List<String> fieldNameList, Class<?> aClass) {
        export(response, list, name, sheet, null, fieldNameList, aClass);
    }

    /**
     * 下载excel
     *
     * @param response      http
     * @param list          需要下载的数据
     * @param name          文件名称
     * @param sheet         表名
     * @param fieldNameList 字段名称列表
     * @param aClass        实体类
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String name, String sheet, AbstractCellStyleStrategy strategy,
                            List<String> fieldNameList, Class<?> aClass) {
        export(response, list, name, sheet, strategy, fieldNameList, aClass);
    }

    private void export(HttpServletResponse response, List<T> list, String name, String sheet, AbstractCellStyleStrategy strategy,
                        List<String> fieldNameList, Class<?> aClass) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcelFactory.write(response.getOutputStream(), aClass)
                    .sheet(sheet)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new TaskUserCellStyleStrategy());
            if (strategy != null) {
                excelWriterSheetBuilder.registerWriteHandler(strategy);
            }
            if (CollectionUtils.isNotEmpty(fieldNameList)) {
                excelWriterSheetBuilder.includeColumnFieldNames(fieldNameList).orderByIncludeColumn(true);
            }
            excelWriterSheetBuilder.doWrite(list);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 下载excel
     *
     * @param response http
     * @param name     文件名称
     */
    public static void exportExcel(HttpServletResponse response, String name, ExcelVO excelVO) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        try (ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(10))
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build()) {
            String sheet = excelVO.getSheet();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheet).head(excelVO.getListsHead()).build();
            excelWriter.write(excelVO.getListsData(), writeSheet);
        } catch (Exception e) {
            throw BusinessException.build(e.getLocalizedMessage());
        }
    }

}
