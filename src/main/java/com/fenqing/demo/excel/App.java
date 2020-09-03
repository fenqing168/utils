package com.fenqing.demo.excel;

import com.fenqing.excel.code.ExcelUtils;
import com.fenqing.io.code.IoUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class App {

    @Test
    public void test() throws IOException {
        byte[] bytes = IoUtils.getBytes("D:\\file\\temp\\excel\\导入\\产品资料检验项目整体导入模板的副本（和阳）.xls");
        ExcelUtils<BaseDataDto> xls = ExcelUtils.getExcelUtil(bytes, "xls", BaseDataDto.class, true);
        BaseDataDto baseDataDto = xls.readData((a, b) -> "图片");
        System.out.println(baseDataDto);
    }

    @Test
    public void test2() throws IOException {
        byte[] bytes = IoUtils.getBytes("D:\\file\\temp\\excel\\导入\\产品资料检验项目整体导入模板的副本（和阳）.xls");
        ExcelUtils<ParamsDto> xls = ExcelUtils.getExcelUtil(bytes, "xls", ParamsDto.class, true);
        List<ParamsDto> paramsDtos = xls.readData2List((a, b) -> "图片");
        paramsDtos.forEach(System.out::println);
    }

    @Test
    public void test3() throws IOException {
        byte[] bytes = IoUtils.getBytes("D:\\file\\temp\\excel\\导入\\产品资料检验项目整体导入模板的副本（和阳）.xls");
        ExcelUtils<ProductImport> xls = ExcelUtils.getExcelUtil(bytes, "xls", ProductImport.class, true);
        ProductImport productImport = xls.readData((a, b) -> "图片");
        System.out.println(productImport);
    }

    @Test
    public void test4() throws IOException {
        byte[] bytes = IoUtils.getBytes("D:\\file\\temp\\excel\\导入\\和阳精密QC数据系统功能需求.xlsx");
        ExcelUtils<ExcelProductBean> xls = ExcelUtils.getExcelUtil(bytes, "xlsx", ExcelProductBean.class, true);
        ExcelProductBean productImport = xls.readData((a, b) -> "图片");
        System.out.println(productImport);
    }

}
