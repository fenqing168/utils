package com.fenqing.excel.code;

import com.fenqing.excel.enumeration.ExcelFileTypeEnum;
import com.fenqing.excel.function.UploadFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 工具类接口
 * @author fenqing
 */
public interface ExcelUtils<T> {

    /**
     * 读取数据
     * @param uploadFile
     * @param <E>
     * @return
     */
    <E> List<T> readData(UploadFile<E> uploadFile);

    /**
     * 获取工具实例
     * @param is
     * @param afx
     * @param tClass
     * @param <E>
     * @return
     */
    static <E> ExcelUtils<E> getExcelUtil(InputStream is, String afx, Class<E> tClass){
        return getExcelUtil(is, afx, tClass, false);
    }

    /**
     * 获取工具实例
     * @param is
     * @param afx
     * @param tClass
     * @param clearEmpty
     * @param <E>
     * @return
     */
    static <E> ExcelUtils<E> getExcelUtil(InputStream is, String afx, Class<E> tClass, boolean clearEmpty){
        switch (ExcelFileTypeEnum.findType(afx)){
            case XLS:
                return new ExcelXlsUtils<>(is, tClass, clearEmpty);
            default:
                return new ExcelXlsxUtils<>(is, tClass, clearEmpty);
        }
    }

    /**
     * 获取工具实例
     * @param bytes
     * @param afx
     * @param tClass
     * @param clearEmpty
     * @param <E>
     * @return
     */
    static <E> ExcelUtils<E> getExcelUtil(byte[] bytes, String afx, Class<E> tClass, boolean clearEmpty){
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        switch (ExcelFileTypeEnum.findType(afx)){
            case XLS:
                return new ExcelXlsUtils<>(bais, tClass, clearEmpty);
            default:
                return new ExcelXlsxUtils<>(bais, tClass, clearEmpty);
        }
    }

}



