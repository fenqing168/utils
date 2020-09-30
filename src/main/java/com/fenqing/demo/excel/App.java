package com.fenqing.demo.excel;

import com.fenqing.excel.bean.ExcelPageList;
import com.fenqing.excel.bean.ExcelTemplateForCodeGrammar;
import com.fenqing.excel.bean.ExcelTemplateForGrammar;
import com.fenqing.excel.code.ExcelTemplateFillUtils;
import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import com.fenqing.io.code.IoUtils;
import com.fenqing.object.code.DataUtils;
import com.fenqing.object.code.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author fenqing
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        test1();
//        test2();
    }

    private static Map<String, Object> getData() {
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("productNo", "产品");
        map.put("iqcNo", "编号");
        map.put("createTime", new Date());
        map.put("inspectorName", "管理员");
        ExcelPageList paramRecords = new ExcelPageList(10, "x");
        for (int i = 0; i < 20; i++) {
            Map<String, Object> maps = new HashMap<>(8);
            maps.put("inspectionTypeName", "inspectionTypeName" + i);
            maps.put("inspectionName", "inspectionName" + i);
            maps.put("chkDevName", "chkDevName" + i);
            maps.put("prodUnit", "prodUnit" + i);
            maps.put("standard", "standard" + i);
            maps.put("sl", "sl" + i);
            maps.put("usl", "usl" + i);
            maps.put("lsl", "lsl" + i);
            maps.put("result", "result" + i);
            paramRecords.add(maps);
        }
        map.put("paramRecords", paramRecords);
        return map;
    }

    private static void test1() throws FileNotFoundException {
        File file = new File("D:\\file\\temp\\excel\\测试\\iqc-parameter-report1.xls");
        InputStream xls = ExcelTemplateFillUtils.getInstance(file, "xls").fill(getData());
        IoUtils.copy(xls, new FileOutputStream(new File(file.getParent(), System.currentTimeMillis() + ".xls")));
    }

    private static void test2() throws FileNotFoundException {
        File file = new File("D:\\file\\temp\\excel\\测试\\iqc-parameter-report1.xlsx");
        InputStream xls = ExcelTemplateFillUtils.getInstance(file, "xlsx").fill(getData());
        IoUtils.copy(xls, new FileOutputStream(new File(file.getParent(), System.currentTimeMillis() + ".xlsx")));
    }

}
