package com.fenqing.demo.excel;

import com.fenqing.excel.bean.ExcelPageList;
import com.fenqing.excel.bean.ExcelTemplateForCodeGrammar;
import com.fenqing.excel.bean.ExcelTemplateForGrammar;
import com.fenqing.excel.code.ExcelTemplateFillUtils;
import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import com.fenqing.io.code.IoUtils;
import com.fenqing.object.code.DataUtils;
import com.fenqing.object.code.MapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class App {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("D:\\file\\temp\\excel\\测试\\iqc-parameter-report1.xls");
        Map<String, Object> data = getData();
        InputStream xls = ExcelTemplateFillUtils.getInstance(file, "xls").fill(data);
        IoUtils.copy(xls, new FileOutputStream("D:\\file\\temp\\excel\\测试\\测试" + System.currentTimeMillis() + ".xls"));

    }

    private static Map<String, Object> getData() {
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("productNo", "产品编号");
        map.put("iqcNo", "编号");
        map.put("createTime", new Date());
        map.put("inspectorName", "管理员");
        return map;
    }
}
