package com.fenqing.excel.code;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
}
