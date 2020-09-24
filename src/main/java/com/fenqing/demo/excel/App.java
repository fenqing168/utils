package com.fenqing.demo.excel;

import com.fenqing.excel.bean.ExcelPageList;
import com.fenqing.excel.bean.ExcelTemplateForCodeGrammar;
import com.fenqing.excel.bean.ExcelTemplateForGrammar;
import com.fenqing.excel.code.ExcelTemplateFillUtils;
import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import com.fenqing.io.code.IoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class App {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, FileNotFoundException {
        File file = new File("C:\\Users\\fenqing\\Desktop\\新建文件夹\\base-iqc-report2.xls");
        ExcelTemplateFillUtils xls = ExcelTemplateFillUtils.getInstance(file, "xls");
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("name", "张三");
        map.put("inspNo", UUID.randomUUID().toString());
        map.put("inspTime", new Date());
        map.put("user", "管理员");
        map.put("reportDate", new Date());
        ExcelPageList inspNames = new ExcelPageList(10, "x");
        for (int i = 0; i < 20; i++) {
            Map<String, Object> temp = new HashMap<>(8);
            temp.put("name", "测试检验项目名称" + i);
            temp.put("a", "测试检验项目" + i);
            temp.put("facility", "测试检验设备" + i);
            temp.put("unit", "测试检验单位" + i);
            temp.put("standard", "测试检验标准" + i);
            temp.put("sl", "测试检验sl" + i);
            temp.put("usl", "测试检验usl" + i);
            temp.put("lsl", "测试检验lsl" + i);
            temp.put("result", "测试检验结果" + i);
            inspNames.add(temp);
        }
        map.put("insp", inspNames);

        ExcelPageList testCode = new ExcelPageList(10, new int[]{0, 1}, "x");
        testCode.add(ExcelTemplateForGrammar.getInstance(ExcelTemplateGrammarEnum.FOR_Y, "values", "no"));
        testCode.add(ExcelTemplateForGrammar.getInstance(ExcelTemplateGrammarEnum.FOR_Y, "values", "result"));
        for (int i = 0; i < inspNames.size(); i++) {
            testCode.add(ExcelTemplateForGrammar.getInstance(ExcelTemplateGrammarEnum.FOR_Y, "values", "index" + i));
        }
        map.put("testCode", testCode);
        ExcelPageList values = new ExcelPageList(20, "y");
        for (int i = 0; i < 40; i++) {
            Map<String, Object> maps = new HashMap<>(8);
            maps.put("no", i);
            maps.put("result", new String[]{"合格", "不合格"}[new Random().nextInt(2)]);
            for (int i1 = 0; i1 < inspNames.size(); i1++) {
                maps.put("index" + i1, "（" + i +"," + i1 + "）" + new Random().nextInt(500));
            }
            values.add(maps);
        }
        map.put("values", values);

        InputStream fill = xls.fill(map);
        IoUtils.copy(fill, new FileOutputStream(new File(file.getParentFile(), System.currentTimeMillis() + ".xls")));
    }
}
