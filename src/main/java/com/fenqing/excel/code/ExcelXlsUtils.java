package com.fenqing.excel.code;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

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
        super(is, clazz, clearEmpty);
        this.clazz = clazz;
        try {
            this.workbook = new HSSFWorkbook(is);
            loadClass();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Map<Integer, Map<Integer, List<PictureData>>> getPictures() {
        Map<Integer, Map<Integer, List<PictureData>>> result = new HashMap<>(8);
        Sheet sheet = workbook.getSheetAt(0);
        HSSFSheet sheet1 = (HSSFSheet) sheet;
        List<HSSFShape> children = sheet1.getDrawingPatriarch().getChildren();
        if(children != null){
            for (HSSFShape shape : children) {
                if (shape instanceof HSSFPicture) {
                    HSSFPicture picture = (HSSFPicture) shape;
                    HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                    int x = cAnchor.getRow1(), y = cAnchor.getCol1();
                    Map<Integer, List<PictureData>> yMap = result.get(x);
                    if(yMap == null){
                        yMap = new HashMap<>(8);
                    }
                    List<PictureData> pictureDatas = yMap.get(y);
                    if(pictureDatas == null){
                        pictureDatas = new ArrayList<>();
                    }
                    pictureDatas.add(picture.getPictureData());
                    yMap.put(y, pictureDatas);
                    result.put(x, yMap);
                }
            }
        }
        return result;
    }
}
