package com.fenqing.excel.code;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xlsx
 * @author fenqing
 */
public class ExcelXlsxUtils<T> extends BaseExcelUtils<T> {

    public ExcelXlsxUtils (InputStream is, Class<T> clazz, boolean clearEmpty) {
        super(is, clazz, clearEmpty);
        this.clazz = clazz;
        try {
            this.workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadClass();
    }

    @Override
    protected Map<Integer, Map<Integer, List<PictureData>>> getPictures() {
        Sheet sheet = workbook.getSheetAt(0);
        XSSFSheet sheet1 = (XSSFSheet) sheet;
        Map<Integer, Map<Integer, List<PictureData>>> result = new HashMap<>(8);
        List<POIXMLDocumentPart> list = sheet1.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    int x = marker.getRow(), y = marker.getCol();
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

