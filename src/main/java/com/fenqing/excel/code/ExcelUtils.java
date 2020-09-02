package com.fenqing.excel.code;

import com.fenqing.demo.excel.BaseDataDto;
import com.fenqing.excel.enumeration.ExcelFileTypeEnum;
import com.fenqing.excel.function.UploadFile;
import com.fenqing.math.code.MathUtils;
import com.fenqing.object.bean.Kv;
import org.apache.poi.hssf.util.CellReference;

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
    <E> List<T> readData2List(UploadFile<E> uploadFile);

    /**
     * 读取数据
     * @param uploadFile
     * @param <E>
     * @return
     */
    <E> T readData(UploadFile<E> uploadFile);

    /**
     * 获取工具实例
     * @param <E>
     * @param bytes
     * @param afx
     * @param tClass
     * @return
     */
    static <E> ExcelUtils<E> getExcelUtil(byte[] bytes, String afx, Class<E> tClass){
        return getExcelUtil(bytes, afx, tClass, false);
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
        return getExcelUtil(bais, afx, tClass, clearEmpty);
    }

    /**
     * 获取坐标
     * @param coordinate
     * @return
     */
    static Kv<Integer, Integer> coordinates(String coordinate){
        CellReference cr = new CellReference(coordinate);
        return Kv.<Integer, Integer>builder().k((int)cr.getCol()).v(cr.getRow()).build();
    }


    /**
     * 字母转数字
     * @param letter
     * @return
     */
    static int letter2Number(String letter){
        int result = 0;
        int lastIndex = letter.length() - 1;
        for(int i = lastIndex; i >= 0; i--){
            char c = letter.charAt(i);
            int num = c - 'A' + 1;
            result += num * Math.pow(26, lastIndex - i);
        }
        return result;
    }

}



