package com.fenqing.excel.code;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.annotation.ExcelTable;
import com.fenqing.excel.bean.FieldConfig;
import com.fenqing.excel.bean.TableConfig;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import com.fenqing.excel.function.UploadFile;
import com.fenqing.math.code.MathUtils;
import com.fenqing.object.bean.Kv;
import com.fenqing.object.code.DataUtils;
import com.fenqing.object.code.ListUtils;
import com.fenqing.object.code.MapUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询
 *
 * @author fenqing
 */
public abstract class BaseExcelUtils<T> implements ExcelUtils<T> {

    protected byte[] bytes;

    protected Workbook workbook;

    protected Class<T> clazz;

    protected int clazzType;

    protected int sheet;

    protected TableConfig tableConfig;

    protected Map<Integer, FieldConfig> fieldConfigMap;

    protected Map<Kv<Integer, Integer>, FieldConfig> objectFieldConfigMap;

    protected List<List<Integer>> mergeCells;

    protected List<Field> fields;

    protected boolean clearEmpty;

    public void init(
            byte[] bytes,
            Workbook workbook,
            Class<T> clazz,
            int clazzType,
            int sheet,
            boolean clearEmpty
    ) {
        this.bytes = bytes;
        this.workbook = workbook;
        this.clazz = clazz;
        this.clazzType = clazzType;
        this.sheet = sheet;
        this.clearEmpty = clearEmpty;
        mergeCells = ListUtils.newArrayList();
        Sheet sheet1 = workbook.getSheetAt(sheet);
        int numMergedRegions = sheet1.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress range = sheet1.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            List<Integer> cell = ListUtils.newArrayList();
            cell.add(firstColumn);
            cell.add(firstRow);
            cell.add(lastColumn);
            cell.add(lastRow);
            mergeCells.add(cell);
        }
    }

    /**
     * 加载
     */
    protected void loadClass() {
        switch (clazzType) {
            case 1:
                fields = ListUtils.newArrayList();
                Field[] declaredFields1 = clazz.getDeclaredFields();
                for (Field field : declaredFields1) {
                    Class<?> type = field.getType();
                    if(Collection.class.isAssignableFrom(type)){
                        Type genericType = field.getGenericType();
                        type = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    }
                    ExcelData annotation = type.getAnnotation(ExcelData.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        fields.add(field);
                    }
                }
                break;
            case 2:
                ExcelData excelData = clazz.getAnnotation(ExcelData.class);
                ExcelDataTypeEnum type = excelData.type();
                Field[] declaredFields;
                switch (type) {
                    case LIST:
                        fieldConfigMap = MapUtils.newHashMap();
                        ExcelTable excelTable = clazz.getAnnotation(ExcelTable.class);
                        String startText = excelTable.startText();
                        String endText = excelTable.endText();
                        Sheet sheet = workbook.getSheetAt(this.sheet);
                        int lastRowNum = sheet.getLastRowNum();
                        TableConfig tableConfig = new TableConfig();
                        int rowStart = -1, rowEnd = -1;
                        for (int i = 0; i <= lastRowNum; i++) {
                            Row row = sheet.getRow(i);
                            short lastCellNum = row.getLastCellNum();
                            for (int j = 0; j < lastCellNum; j++) {
                                Cell cell = row.getCell(j);
                                cell.setCellType(CellType.STRING);
                                String stringCellValue = cell.getStringCellValue();
                                if (startText.equals(stringCellValue) && rowStart == -1) {
                                    rowStart = i + 2;
                                    break;
                                }
                                if (endText.equals(stringCellValue) && rowEnd == -1) {
                                    rowEnd = i - 1;
                                    break;
                                }
                            }
                            if (rowStart != -1 && rowEnd != -1) {
                                break;
                            }
                        }

                        declaredFields = this.clazz.getDeclaredFields();
                        for (Field declaredField : declaredFields) {
                            declaredField.setAccessible(true);
                            ExcelField excelField = declaredField.getAnnotation(ExcelField.class);
                            if (excelField != null) {
                                String x = excelField.x();
                                int xNum = ExcelUtils.letter2Number(x) - 1;
                                FieldConfig fieldConfig = new FieldConfig();
                                fieldConfig.setField(declaredField);
                                fieldConfig.setImg(excelField.img());
                                fieldConfigMap.put(xNum, fieldConfig);
                            }
                        }

                        Set<Integer> integers = fieldConfigMap.keySet();
                        int min = MathUtils.min(integers);
                        int max = MathUtils.max(integers);
                        tableConfig.setXStart(min);
                        tableConfig.setYStart(rowStart);
                        tableConfig.setXEnd(max);
                        tableConfig.setYEnd(rowEnd);
                        this.tableConfig = tableConfig;
                        break;
                    case OBJECT:
                        objectFieldConfigMap = MapUtils.newHashMap();
                        declaredFields = this.clazz.getDeclaredFields();
                        for (Field declaredField : declaredFields) {
                            declaredField.setAccessible(true);
                            ExcelField excelField = declaredField.getAnnotation(ExcelField.class);
                            if (excelField != null) {
                                String coordinate = excelField.coordinate();
                                Kv<Integer, Integer> kv = ExcelUtils.coordinates(coordinate);
                                FieldConfig fieldConfig = new FieldConfig();
                                fieldConfig.setField(declaredField);
                                fieldConfig.setImg(excelField.img());
                                objectFieldConfigMap.put(kv, fieldConfig);
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public <E> List<T> readData2List(UploadFile<E> uploadFile) {
        Map<Integer, Map<Integer, List<PictureData>>> pictures = getPictures();
        Sheet sheet = workbook.getSheetAt(this.sheet);
        List<T> ts = new ArrayList<>();
        for (int i = tableConfig.getYStart(); i <= tableConfig.getYEnd(); i++) {
            try {
                Row row = sheet.getRow(i);
                T o = clazz.newInstance();
                for (int j = tableConfig.getXStart(); j <= tableConfig.getXEnd(); j++) {
                    FieldConfig fieldConfig = fieldConfigMap.get(j);
                    if (fieldConfig == null) {
                        continue;
                    }
                    boolean imgs = fieldConfig.isImg();
                    if (imgs) {
                        List<PictureData> pictureData = null;
                        Map<Integer, List<PictureData>> integerListMap = pictures.get(j);
                        if (integerListMap != null) {
                            pictureData = integerListMap.get(i);
                        }
                        if (pictureData == null) {
                            pictureData = new ArrayList<>();
                        }
                        List<E> urls1 = pictureData.stream()
                                .map(item -> uploadFile.upload(item.getData(), item.getMimeType()))
                                .collect(Collectors.toList());
                        DataUtils.setValue(o, fieldConfig.getField().getName(), urls1);
                        if (urls1.isEmpty()) {
                        }
                    } else {
                        Cell cell = row.getCell(j);
                        cell.setCellType(CellType.STRING);
                        String value = cell.getStringCellValue();
                        DataUtils.setValue(o, fieldConfig.getField().getName(), value);
                    }
                }
                ts.add(o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (clearEmpty) {
            ts = ts.stream().filter(DataUtils::isNotEmpty).collect(Collectors.toList());
        }
        return ts;
    }

    @Override
    public <E> T readData(UploadFile<E> uploadFile) {
        try {
            switch (clazzType) {
                case 1:
                    T t = clazz.newInstance();
                    for (Field field : fields) {
                        Class<?> type = field.getType();
                        if(Collection.class.isAssignableFrom(type)){
                            Type genericType = field.getGenericType();
                            type = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                        }
                        ExcelData annotation = type.getAnnotation(ExcelData.class);
                        ExcelDataTypeEnum type1 = annotation.type();
                        ExcelUtils<?> excelUtil = this.getExcelUtil(bytes, type, this.clearEmpty);
                        switch (type1) {
                            case OBJECT:
                                Object o = excelUtil.readData(uploadFile);
                                DataUtils.setValue(t, field.getName(), o);
                                break;
                            case LIST:
                                List<?> objects = excelUtil.readData2List(uploadFile);
                                DataUtils.setValue(t, field.getName(), objects);
                                break;
                            default:
                                return null;
                        }
                    }
                    return t;
                case 2:
                    return readDataObject(uploadFile);
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <E> T readDataObject(UploadFile<E> uploadFile) {
        Map<Integer, Map<Integer, List<PictureData>>> pictures = getPictures();
        Sheet sheet = workbook.getSheetAt(this.sheet);
        try {
            T t = clazz.newInstance();
            for (Kv<Integer, Integer> k : objectFieldConfigMap.keySet()) {
                FieldConfig v = objectFieldConfigMap.get(k);
                boolean img = v.isImg();
                if (!img) {
                    int x = k.getK();
                    int y = k.getV();
                    Row row = sheet.getRow(x);
                    Cell cell = row.getCell(y);
                    cell.setCellType(CellType.STRING);
                    String stringCellValue = cell.getStringCellValue();
                    v.getField().set(t, stringCellValue);
                } else {
                    int x = k.getK();
                    int y = k.getV();
                    List<Integer> cell = findCell(x, y);
                    List<PictureData> pictureDatas = ListUtils.newArrayList();
                    if (cell.isEmpty()) {
                        pictureDatas = pictures
                                .getOrDefault(x, MapUtils.newHashMap())
                                .getOrDefault(y, ListUtils.newArrayList());
                    } else {
                        int cx = cell.get(0);
                        int cy = cell.get(1);
                        int cx1 = cell.get(2);
                        int cy1 = cell.get(3);
                        for (int i = cx; i <= cx1; i++) {
                            boolean flag = false;
                            for (int j = cy; j <= cy1; j++) {
                                Map<Integer, List<PictureData>> integerListMap = pictures.get(i);
                                if (integerListMap == null) {
                                    continue;
                                }
                                List<PictureData> pictureDatasTemp = integerListMap.get(j);
                                if (pictureDatasTemp == null) {
                                    continue;
                                }
                                pictureDatas = pictureDatasTemp;
                                flag = true;
                                break;
                            }
                            if (flag) {
                                break;
                            }
                        }
                    }
                    List<E> collect = pictureDatas.stream().map(pictureData ->
                            uploadFile.upload(pictureData.getData(), pictureData.getMimeType())
                    ).collect(Collectors.toList());
                    v.getField().set(t, collect);
                }

            }
            return t;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片
     *
     * @return
     */
    protected abstract Map<Integer, Map<Integer, List<PictureData>>> getPictures();


    private List<Integer> findCell(int cell, int row) {
        for (List<Integer> mergeCell : mergeCells) {
            int x = mergeCell.get(0);
            int y = mergeCell.get(1);
            int x1 = mergeCell.get(2);
            int y1 = mergeCell.get(3);
            if (x <= cell && x1 >= cell && y <= row && y1 >= row) {
                return mergeCell;
            }
        }
        return new ArrayList<>();
    }


    protected abstract <TT> ExcelUtils<TT> getExcelUtil(byte[] bytes, Class<TT> tClass, boolean clearEmpty);
}
