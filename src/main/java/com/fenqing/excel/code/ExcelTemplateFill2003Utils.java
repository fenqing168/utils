package com.fenqing.excel.code;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Random;

/**
 * 2003excel
 * @author fenqing
 */
public class ExcelTemplateFill2003Utils extends BaseExcelTemplateFillUtils {

    private Random random = new Random();

    public ExcelTemplateFill2003Utils(InputStream is) {
        try {
            this.workbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2003Utils(File file) {
        try {
            this.workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2003Utils(byte[] bytes) {
        try {
            this.workbook = new HSSFWorkbook(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2003Utils(String url) {
        try {
            this.workbook = new HSSFWorkbook(new URL(url).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setColor(Cell cell, int[] rgb) {
        HSSFPalette customPalette = ((HSSFWorkbook) workbook).getCustomPalette();
        HSSFColor hssfColor = customPalette.findSimilarColor((byte) rgb[0], (byte) rgb[1], (byte) rgb[2]);
        CellStyle cellStyle = cell.getCellStyle();
        Font font = workbook.createFont();
        font.setColor(hssfColor.getIndex());
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    @Override
    public void setBgColor(Cell cell, int[] rgb) {
        HSSFPalette customPalette = ((HSSFWorkbook) workbook).getCustomPalette();
        HSSFColor hssfColor = customPalette.findSimilarColor((byte) rgb[0], (byte) rgb[1], (byte) rgb[2]);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(hssfColor.getIndex());
        cell.setCellStyle(cellStyle);
    }
}
