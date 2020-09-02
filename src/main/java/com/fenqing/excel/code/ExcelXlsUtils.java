package com.fenqing.excel.code;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelSheet;
import com.fenqing.io.code.IoUtils;
import com.fenqing.object.code.ListUtils;
import com.fenqing.object.code.MapUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xls工具
 * @author fenqing
 */
public class ExcelXlsUtils<T> extends BaseExcelUtils<T> {

    public ExcelXlsUtils(InputStream is, Class<T> clazz, boolean clearEmpty) {
        byte[] bytes = IoUtils.getBytes(is);
        Workbook workbook;
        int clazzType;
        int sheet;
        ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
        ExcelData excelData = clazz.getAnnotation(ExcelData.class);
        if(excelSheet != null){
            clazzType = 1;
            sheet = excelSheet.sheet();
        }else if(excelData != null){
            clazzType = 2;
            sheet = excelData.sheet();
        }else{
            throw new RuntimeException("类上请标注@ExcelSheet或者@ExcelData");
        }
        try {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        init(
                bytes,
                workbook,
                clazz,
                clazzType,
                sheet,
                clearEmpty
        );
        loadClass();
    }

    @Override
    protected Map<Integer, Map<Integer, List<PictureData>>> getPictures() {
        Map<Integer, Map<Integer, List<PictureData>>> result = new HashMap<>(8);
        Sheet sheet = workbook.getSheetAt(0);
        HSSFSheet sheet1 = (HSSFSheet) sheet;
        List<HSSFShape> children = sheet1.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : children) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                int x = cAnchor.getCol1(), y = cAnchor.getRow1();
                Map<Integer, List<PictureData>> xMap = result.getOrDefault(x, MapUtils.newHashMap());
                List<PictureData> pictureDatas = xMap.getOrDefault(y, ListUtils.newArrayList());
                pictureDatas.add(picture.getPictureData());
                xMap.put(y, pictureDatas);
                result.put(x, xMap);
            }
        }
        return result;
    }

    @Override
    protected <TT> ExcelUtils<TT> getExcelUtil(byte[] bytes, Class<TT> tClass, boolean clearEmpty) {
        return ExcelUtils.getExcelUtil(bytes, "xls", tClass, clearEmpty);
    }
}
