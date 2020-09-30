package com.fenqing.excel.code;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * 2007excel
 * @author fenqing
 */
public class ExcelTemplateFill2007Utils extends BaseExcelTemplateFillUtils {
    public ExcelTemplateFill2007Utils(InputStream is) {
        try {
            this.workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2007Utils(File file) {
        try {
            this.workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2007Utils(byte[] bytes) {
        try {
            this.workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExcelTemplateFill2007Utils(String url) {
        try {
            this.workbook = new XSSFWorkbook(new URL(url).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setColor(Cell cell, int[] rgb) {
        XSSFColor xssfColor = new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]));
        CellStyle cellStyle = cell.getCellStyle();
        Font font = workbook.createFont();
        font.setColor(xssfColor.getIndex());
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    @Override
    public void setBgColor(Cell cell, int[] rgb) {
        XSSFColor xssfColor = new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]));
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(xssfColor.getIndex());
        cell.setCellStyle(cellStyle);
    }
}
