package com.fenqing.excel.code;

import com.fenqing.io.code.IoUtils;
import com.fenqing.object.code.DataUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * excel数据填充
 * @author fenqing
 */
public abstract class ExcelTemplateUtils {

    /**
     * 导出
     * @return
     */
    public static ExcelTemplateUtils getInstance(String type){
        //将文件类型大写
        type = type.toUpperCase();
        //根据文件类型选择创建实例
        switch (type){
            case "XLSX":
                return ExcelTemplateXlsxUtils.getInstance();
            default:
                return ExcelTemplateXlsUtils.getInstance();
        }
    }

    /**
     * 导出
     * @return
     */
    public static InputStream fill(String path, Map<String, Object> data){
        String type = path.substring(path.lastIndexOf(".") + 1);
        ClassPathResource resource = new ClassPathResource(path);
        try {
            InputStream is = resource.getInputStream();
            return getInstance(type).fill(is, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取工作簿对象
     * @param is
     * @return
     */
    protected abstract Workbook getWorkbook(InputStream is);

    /**
     * 导出
     * @param is
     * @param data
     * @return
     */
    private InputStream fill(InputStream is, Map<String, Object> data){
        Workbook workbook = getWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Font font = workbook.createFont();
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("m/d/yy h:mm"));
        dateStyle.setAlignment(HorizontalAlignment.CENTER);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font1 = workbook.createFont();
        font1.setFontName("Helvetica");

        generatingCode(workbook, sheet, data);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析结构语法
     * @param workbook
     * @param sheet
     * @param data
     */
    private void generatingCode(Workbook workbook, Sheet sheet, Map<String, Object> data){
        int lastRowNum = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                short lastCellNum = row.getLastCellNum();
                for (short cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                        String boundKey = parseStructuralGrammar(cell.getStringCellValue());
                        if (boundKey != null) {
                            String[] move = getMove(boundKey);
                            if (move != null) {
                                int x = cellIndex;
                                int y = rowIndex;
                                Iterator<?> iterator = ((Iterable<?>) DataUtils.getValue(data, move[0])).iterator();
                                while (iterator.hasNext()){
                                    Object next = iterator.next();
                                    setCellValue(workbook, workbook.getSheetAt(0).getRow(y).getCell(x), next);
                                    if("x".equalsIgnoreCase(move[1])){
                                        x++;
                                    }else{
                                        y++;
                                    }
                                }
                                break;
                            }else{
                                Object boundvalue = DataUtils.getValue(data, boundKey);
                                setCellValue(workbook, cell, boundvalue);
                                cell.removeCellComment();
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 获取移动方向
     * @param boundKey
     * @return
     */
    protected String[] getMove(String boundKey){
        String separator = "->";
        if(boundKey.contains(separator)){
            return boundKey.split(separator);
        }
        return null;
    }

    /**
     * 是否为结构语法
     */
    private String parseStructuralGrammar(String text){
        if(text.matches("^#\\{\\{.+\\}\\}")){
            return text.replaceAll("[#\\{\\}]", "");
        }
        return null;
    }

    /**
     * 设置单元格值
     * @param workbook
     * @param cell
     * @param value
     */
    private static void setCellValue(Workbook workbook, Cell cell, Object value) {
        if (value instanceof Date) {
            CellStyle style = cell.getCellStyle();
            style.setDataFormat(workbook.createDataFormat().getFormat("m/d/yy h:mm"));
            Date date = (Date) value;
            long time = date.getTime() + 8 * 60 * 60 * 1000;
            cell.setCellValue(new Date(time));
        } else if (value != null) {
            cell.setCellValue(String.valueOf(value));
        } else {
            cell.setCellValue("--");
        }
    }
}
