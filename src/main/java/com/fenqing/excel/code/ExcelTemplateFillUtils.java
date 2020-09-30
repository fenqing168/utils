package com.fenqing.excel.code;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel模板数据填充
 * @author fenqing
 */
public interface ExcelTemplateFillUtils {

    String XLS = "XLS";

    String S = "0123456789ABCDEF";

    Pattern PATTERN = Pattern.compile("^[0-9A-F]{3}|[0-9A-F]{6}$");

    Pattern PATTERN1 = Pattern.compile("\\$\\{.+}");

    /**
     * 数据填充
     * @param data 数据
     * @return
     */
    InputStream fill(Map<String, Object> data);

    /**
     * 获取实例
     * @param is
     * @param type
     * @return
     */
    static ExcelTemplateFillUtils getInstance(InputStream is, String type){
        return getInstance((Object)is, type);
    }

    /**
     * 获取实例
     * @param file
     * @param type
     * @return
     */
    static ExcelTemplateFillUtils getInstance(File file, String type){
        return getInstance((Object)file, type);
    }

    /**
     * 获取实例
     * @param bytes
     * @param type
     * @return
     */
    static ExcelTemplateFillUtils getInstance(byte[] bytes, String type){
        return getInstance((Object)bytes, type);
    }

    /**
     * 获取实例
     * @param url
     * @param type
     * @return
     */
    static ExcelTemplateFillUtils getInstance(String url, String type){
        return getInstance((Object)url, type);
    }

    /**
     * 获取实例
     * @param obj
     * @param type
     * @return
     */
    static ExcelTemplateFillUtils getInstance(Object obj, String type){
        type = type.toUpperCase();
        Class<? extends ExcelTemplateFillUtils> clazz;
        if(XLS.equals(type)){
            clazz = ExcelTemplateFill2003Utils.class;
        }else{
            clazz = ExcelTemplateFill2007Utils.class;
        }
        try {
            Constructor<? extends ExcelTemplateFillUtils> constructor;
            if(obj instanceof InputStream){
                constructor = clazz.getConstructor(InputStream.class);
            }else{
                constructor = clazz.getConstructor(obj.getClass());
            }

            return constructor.newInstance(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 十六进制转换成RGB
     *
     * @param hex
     * @return
     */
    static int[] convertHexToRgb(String hex) {
        int[] rgb = new int[3];
        if (hex != null) {
            hex = hex.toUpperCase();
            if (hex.substring(0, 1).equals("#")) {
                hex = hex.substring(1);
            }
            if (PATTERN.matcher(hex).matches()) {
                for (int i = 0; i < 3; i++) {
                    String a = hex.length() == 6 ? hex.substring(i * 2, i * 2 + 2) : hex.substring(i, i + 1) + hex.substring(i, i + 1);
                    String c = a.substring(0, 1);
                    String d = a.substring(1, 2);
                    rgb[i] = Integer.parseInt(String.valueOf(S.indexOf(c) * 16 + S.indexOf(d)));
                }
            }
        }
        return rgb;
    }

    /**
     * 字符串转代码
     * @param code
     * @param data
     * @return
     */
    static String string2JsCode(String code, Map<String, Object> data) {
        Matcher matcher = PATTERN1.matcher(code);
        while (matcher.find()){
            String group = matcher.group();
            String key = group.replaceAll("[${}]", "").trim();
            Object o = data.get(key);
            group = group.replaceAll("([${}])", "\\\\$1");
            if(o == null){
                code = code.replaceAll(group, "null");
            }else if(o instanceof String){
                code = code.replaceAll(group, "'" + o +"'");
            }else if(o instanceof Date){
                code = code.replaceAll(group, "new Date(" + ((Date) o).getTime() +")");
            }else{
                code = code.replaceAll(group, o.toString());
            }
        }
        return code;
    }

}
