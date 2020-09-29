package com.fenqing.excel.code;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author fenqing
 */
public class ExcelTemplateXlsUtils extends BaseExcelTemplateUtils {

    private ExcelTemplateXlsUtils(){}

    @Override
    protected Workbook getWorkbook(InputStream is) {
        try {
            return new HSSFWorkbook(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final static class ExcelTemplateXlsInstance{
        private static final ExcelTemplateXlsUtils INSTANCE = new ExcelTemplateXlsUtils();
    }

    public static ExcelTemplateXlsUtils getInstance(){
        return ExcelTemplateXlsInstance.INSTANCE;
    }

}
