package com.fenqing.excel.code;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * excel模板数据填充
 * @author fenqing
 */
public interface ExcelTemplateFillUtils {

    String XLS = "XLS";

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

}
