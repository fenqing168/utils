package com.fenqing.excel.code;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.net.URL;

/**
 * 2003excel
 * @author fenqing
 */
public class ExcelTemplateFill2003Utils extends BaseExcelTemplateFillUtils {
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
}
