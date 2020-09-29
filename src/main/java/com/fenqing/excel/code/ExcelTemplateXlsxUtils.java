package com.fenqing.excel.code;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author fenqing
 */
public class ExcelTemplateXlsxUtils extends BaseExcelTemplateUtils {

    private ExcelTemplateXlsxUtils(){}

    @Override
    protected Workbook getWorkbook(InputStream is) {
        try {
            return new XSSFWorkbook(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final static class ExcelTemplateXlsxInstance{
        private static final ExcelTemplateXlsxUtils INSTANCE = new ExcelTemplateXlsxUtils();
    }

    public static ExcelTemplateXlsxUtils getInstance(){
        return ExcelTemplateXlsxInstance.INSTANCE;
    }

}
