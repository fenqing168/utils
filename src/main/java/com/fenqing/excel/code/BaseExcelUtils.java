package com.fenqing.excel.code;

import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.annotation.ExcelTable;
import com.fenqing.excel.bean.FieldConfig;
import com.fenqing.excel.bean.TableConfig;
import com.fenqing.excel.function.UploadFile;
import com.fenqing.object.code.DataUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询
 * @author fenqing
 */
public abstract class BaseExcelUtils<T> implements ExcelUtils<T> {

    protected Workbook workbook;

    protected Class<T> clazz;

    protected TableConfig tableConfig;

    protected Map<Integer, FieldConfig> fieldConfigMap;

    protected boolean clearEmpty;

    public BaseExcelUtils(InputStream is, Class<T> clazz, boolean clearEmpty) {
        this.clazz = clazz;
        this.clearEmpty = clearEmpty;
    }

    protected void loadClass(){
        ExcelTable annotation = clazz.getAnnotation(ExcelTable.class);
        if(annotation == null){
            throw new RuntimeException("没有检测到Table注解");
        }
        String startText = annotation.startText();
        String endText = annotation.endText();
        int leftStart = annotation.leftStart();
        int width = annotation.width();
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 0, flag = 0, startRow = 0, endRow = 0;
        while(rowIndex <= 200){
            Row row = sheet.getRow(rowIndex);
            int end = leftStart + width;
            for(int i = leftStart; i < end; i++){
                Cell cell = row.getCell(i);
                cell.setCellType(CellType.STRING);
                String value = cell.getStringCellValue();
                value = value.trim();
                if (flag == 0 && startText.equals(value)) {
                    flag++;
                    startRow = rowIndex;
                    break;
                }
                if (flag == 1 && endText.equals(value)) {
                    flag++;
                    endRow = rowIndex;
                    break;
                }
            }
            if(flag == 2){
                break;
            }
            rowIndex++;
        }
        tableConfig = new TableConfig();
        tableConfig.setXStart(leftStart);
        tableConfig.setXEnd(leftStart + width);
        tableConfig.setYStart(startRow);
        tableConfig.setYEnd(endRow);
        tableConfig.setClazz(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();
        fieldConfigMap = new HashMap<>(8);
        for (Field field : declaredFields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if(excelField != null){
                if(excelField.isImgs()){
                    if(!(field.getType().equals(List.class))){
                        throw new RuntimeException("图片字段请设置成List");
                    }
                }
                FieldConfig fieldConfig = new FieldConfig();
                field.setAccessible(true);
                fieldConfig.setField(field);
                fieldConfig.setImgs(excelField.isImgs());
                fieldConfigMap.put(excelField.sort(), fieldConfig);
            }
        }

    }

    @Override
    public <E> List<T> readData(UploadFile<E> uploadFile) {
        Map<Integer, Map<Integer, List<PictureData>>> pictures = getPictures();
        Sheet sheet = workbook.getSheetAt(0);
        List<T> ts = new ArrayList<>();
        for(int i = tableConfig.getYStart() + 2; i < tableConfig.getYEnd(); i++){
            try {
                Row row = sheet.getRow(i);
                T o = clazz.newInstance();
                int offset = 0, valid = 0;
                for(int j = tableConfig.getXStart(); j < tableConfig.getXEnd(); j++){
                    FieldConfig fieldConfig = fieldConfigMap.get(offset);
                    if(fieldConfig == null){
                        offset++;
                        continue;
                    }
                    boolean imgs = fieldConfig.isImgs();
                    if(imgs){
                        List<PictureData> pictureData = null;
                        Map<Integer, List<PictureData>> integerListMap = pictures.get(i);
                        if(integerListMap != null){
                            pictureData = integerListMap.get(j);
                        }
                        if(pictureData == null){
                            pictureData = new ArrayList<>();
                        }
                        List<E> urls1 = pictureData.stream().map(item -> uploadFile.upload(item.getData(), item.getMimeType())).collect(Collectors.toList());
                        DataUtils.setValue(o, fieldConfig.getField().getName(), urls1);
                        if(urls1.isEmpty()){
                            valid++;
                        }
                    }else{
                        Cell cell = row.getCell(j);
                        cell.setCellType(CellType.STRING);
                        String value = cell.getStringCellValue();
                        DataUtils.setValue(o, fieldConfig.getField().getName(), value);
                        if(value == null || value.isEmpty()){
                            valid++;
                        }
                    }
                    offset++;
                }
                if(valid < fieldConfigMap.size()){
                    ts.add(o);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(clearEmpty){
            ts = ts.stream().filter(DataUtils::isNotEmpty).collect(Collectors.toList());
        }
        return ts;
    }

    /**
     * 获取图片
     * @return
     */
    protected abstract Map<Integer, Map<Integer, List<PictureData>>> getPictures();

}
