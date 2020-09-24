package com.fenqing.excel.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenqing.excel.bean.ExcelPageList;
import com.fenqing.excel.bean.ExcelTemplateForCodeGrammar;
import com.fenqing.excel.bean.ExcelTemplateForGrammar;
import com.fenqing.excel.bean.ExcelTemplateGrammar;
import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import com.fenqing.object.code.ArrayUtils;
import com.fenqing.object.code.DataUtils;
import com.fenqing.object.code.StringUtils;
import com.fenqing.time.code.TimeUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 抽象层
 *
 * @author fenqing
 */
public abstract class BaseExcelTemplateFillUtils implements ExcelTemplateFillUtils {

    /**
     * excel工作簿
     */
    protected Workbook workbook;

    @Override
    public InputStream fill(Map<String, Object> data) {
        data = new HashMap<>(data);
        //扫描遍历y的，并生成代码
        List<List<Map<String, String>>> xyKeyMaps = createSheet(data);
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet next = sheetIterator.next();
            scannerForCode(next, data, "y");
            scannerForCode(next, data, "x");
            replace(data, xyKeyMaps.get(0), xyKeyMaps.get(1));
            scannerFor(next, data, "y");
            scannerFor(next, data, "x");
            scannerText(next, data);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data
     * 生成工作表
     */
    private List<List<Map<String, String>>> createSheet(Map<String, Object> data) {
        Map<String, ExcelPageList> pageListMap = new HashMap<>(8);
        Set<String> keys = data.keySet();
        for (String key : keys) {
            Object val = data.get(key);
            if (val instanceof ExcelPageList) {
                pageListMap.put(key, (ExcelPageList) val);
            }
        }
        List<Map<String, String>> xKeyMaps = new ArrayList<>();
        List<Map<String, String>> yKeyMaps = new ArrayList<>();
        for (String key : pageListMap.keySet()) {
            ExcelPageList pageList = pageListMap.get(key);
            int pageSize = pageList.getPageSize();
            int[] repetitionItemIndexs = pageList.getRepetitionItemIndexs();
            String direction = pageList.getDirection();


            if ("x".equals(direction)) {
                List<Object> os = new ArrayList<>();
                ExcelPageList newPageList = new ExcelPageList(pageSize, direction);
                if (repetitionItemIndexs != null) {
                    for (int repetitionItemIndex : repetitionItemIndexs) {
                        os.add(pageList.get(repetitionItemIndex));
                    }
                    for (int i = 0; i < pageList.size(); i++) {
                        boolean contains = ArrayUtils.contains(repetitionItemIndexs, i);
                        if (!contains) {
                            newPageList.add(pageList.get(i));
                        }
                    }
                    pageList = newPageList;
                } else {
                    repetitionItemIndexs = new int[]{};
                }

                for (int i = 0, j = 0; i < pageList.size(); i += pageSize, j++) {
                    if (xKeyMaps.size() <= j) {
                        xKeyMaps.add(new HashMap<>(8));
                    }
                    Map<String, String> xKeyMap = xKeyMaps.get(j);
                    String uuid = UUID.randomUUID().toString();
                    xKeyMap.put(key, uuid);
                    List<Object> temp;
                    if (i + pageSize > pageList.size()) {
                        temp = new ArrayList<>(pageList.subList(i, pageList.size() - 1));
                    } else {
                        temp = new ArrayList<>(pageList.subList(i, i + pageSize));
                    }
                    for (int z = 0; z < repetitionItemIndexs.length; z++) {
                        temp.add(repetitionItemIndexs[z], os.get(z));
                    }
                    data.put(uuid, temp);
                }
            }
            if ("y".equals(direction)) {
                for (int i = 0, j = 0; i < pageList.size(); i += pageSize, j++) {
                    if (yKeyMaps.size() <= j) {
                        yKeyMaps.add(new HashMap<>(8));
                    }
                    Map<String, String> yKeyMap = yKeyMaps.get(j);
                    String uuid = UUID.randomUUID().toString();
                    yKeyMap.put(key, uuid);
                    List<Object> temp;
                    if (i + pageSize > pageList.size()) {
                        temp = new ArrayList<>(pageList.subList(i, pageList.size() - 1));
                    } else {
                        temp = new ArrayList<>(pageList.subList(i, i + pageSize));
                    }
                    data.put(uuid, temp);
                }
            }
        }
        Sheet nowSheet = workbook.getSheetAt(0);
        //替换
        for (Map<String, String> xKeyMap : xKeyMaps) {
            for (int yi = 0; yi < yKeyMaps.size(); yi++) {
                Map<String, String> yKeyMap = yKeyMaps.get(yi);
                Sheet sheet = workbook.createSheet();
                copySheet(nowSheet, sheet, nowSheet.getFirstRowNum(), nowSheet.getLastRowNum());
                int lastRowNum = sheet.getLastRowNum();
                for (int i = 0; i < lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        short lastCellNum = row.getLastCellNum();
                        for (int j = 0; j < lastCellNum; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                String value = cell.getStringCellValue();
                                ExcelTemplateGrammar analyse = analyse(value, ExcelTemplateGrammarEnum.FOR_X);
                                if (analyse == null) {
                                    analyse = analyse(cell.getStringCellValue(), ExcelTemplateGrammarEnum.FOR_CODE_X);
                                }
                                if (analyse != null) {
                                    String key = analyse.getKey();
                                    if (xKeyMap.containsKey(key)) {
                                        String s = value.replaceAll(key, xKeyMap.get(key));
                                        cell.setCellValue(s);
                                    }
                                }
                                analyse = analyse(value, ExcelTemplateGrammarEnum.FOR_Y);
                                if (analyse == null) {
                                    analyse = analyse(cell.getStringCellValue(), ExcelTemplateGrammarEnum.FOR_CODE_Y);
                                }
                                if (analyse != null) {
                                    String key = analyse.getKey();
                                    if (yKeyMap.containsKey(key)) {
                                        String s = value.replaceAll(key, yKeyMap.get(key));
                                        cell.setCellValue(s);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        workbook.removeSheetAt(0);
        return new ArrayList<List<Map<String, String>>>(){{
            add(xKeyMaps);
            add(yKeyMaps);
        }};
    }

    private void replace(Map<String, Object> data, List<Map<String, String>> xKeyMaps, List<Map<String, String>> yKeyMaps){
        //替换
        int sheetNum = 0;
        for (Map<String, String> xKeyMap : xKeyMaps) {
            for (int yi = 0; yi < yKeyMaps.size(); yi++) {
                Map<String, String> yKeyMap = yKeyMaps.get(yi);
                Sheet sheet = workbook.getSheetAt(sheetNum++);
                int lastRowNum = sheet.getLastRowNum();
                for (int i = 0; i < lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        short lastCellNum = row.getLastCellNum();
                        for (int j = 0; j < lastCellNum; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                String value = cell.getStringCellValue();
                                ExcelTemplateGrammar analyse = analyse(value, ExcelTemplateGrammarEnum.FOR_Y);
                                if (analyse == null) {
                                    analyse = analyse(cell.getStringCellValue(), ExcelTemplateGrammarEnum.FOR_CODE_Y);
                                }
                                if (analyse != null) {
                                    String key = analyse.getKey();
                                    if (yKeyMap.containsKey(key)) {
                                        String s = value.replaceAll(key, yKeyMap.get(key));
                                        cell.setCellValue(s);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增sheet，并且复制sheet内容到新增的sheet里
     * @param fromSheet
     * @param newSheet
     * @param firstRow
     * @param lastRow
     */
    private void copySheet(Sheet fromSheet, Sheet newSheet, int firstRow, int lastRow) {
        // 复制一个单元格样式到新建单元格
        if ((firstRow == -1) || (lastRow == -1) || lastRow < firstRow) {
            return;
        }
        // 复制合并的单元格
        for (int i = 0; i < fromSheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = fromSheet.getMergedRegion(i);
            if ((region.getFirstRow() >= firstRow) && (region.getLastRow() <= lastRow)) {
                newSheet.addMergedRegion(region);
            }
        }

        // 设置列宽
        for (int i = firstRow; i < lastRow; i++) {
            Row fromRow = fromSheet.getRow(i);
            Row newRow = newSheet.getRow(i);
            if (fromRow != null) {
                short firstCellNum = fromRow.getFirstCellNum();
                short lastCellNum = fromRow.getLastCellNum();
                for (int j = firstCellNum; j <= lastCellNum; j++) {
                    int colnum = fromSheet.getColumnWidth((short) j);
                    newSheet.setColumnWidth((short) j, (short) colnum);
                    if (colnum == 0) {
                        newSheet.setColumnHidden((short) j, true);
                    } else {
                        newSheet.setColumnHidden((short) j, false);
                    }
                }
            }
        }

        Cell newCell;
        Cell fromCell;
        // 复制行并填充数据
        for (int i = 0; i < lastRow; i++) {
            Row fromRow = fromSheet.getRow(i);
            if (fromRow == null) {
                continue;
            }
            Row newRow = newSheet.createRow(i - firstRow);
            newRow.setHeight(fromRow.getHeight());
            for (int j = fromRow.getFirstCellNum(); j < fromRow.getPhysicalNumberOfCells(); j++) {
                fromCell = fromRow.getCell((short) j);
                if (fromCell == null) {
                    continue;
                }
                newCell = newRow.createCell((short) j);
                newCell.setCellStyle(fromCell.getCellStyle());
                int cType = fromCell.getCellType();
                newCell.setCellType(cType);
                switch (cType) {
                    case Cell.CELL_TYPE_STRING:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(fromCell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        newCell.setCellValue(fromCell.getCellFormula());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(fromCell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        newCell.setCellValue(fromCell.getErrorCellValue());
                        break;
                    default:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                }
            }
        }
    }

    private void scannerForCode(Sheet sheet, Map<String, Object> data, String move) {
        //最大行数
        int lastRowNum = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            //获取行
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                //获取本行最大单元格数
                short lastCellNum = row.getLastCellNum();
                for (short cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //读取
                    if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                        ExcelTemplateGrammar analyse = analyse(cell.getStringCellValue(),
                                "x".equals(move)
                                        ? ExcelTemplateGrammarEnum.FOR_CODE_X
                                        : ExcelTemplateGrammarEnum.FOR_CODE_Y
                        );
                        if (analyse != null) {
                            ExcelTemplateForCodeGrammar grammar = (ExcelTemplateForCodeGrammar) analyse;
                            int x = cellIndex, y = rowIndex;
                            //获取数组对象
                            String key = grammar.getKey();
                            Collection<?> collection = (Collection<?>) DataUtils.getValue(data, key);
                            collection = collection == null ? new ArrayList<>() : collection;
                            //移动行，来实现插入的功能
                            if (ExcelTemplateGrammarEnum.FOR_CODE_Y == grammar.getGrammar()) {
                                sheet.shiftRows(y + 1, lastRowNum, collection.size() - 1);
                            }
                            cell.setCellValue("");
                            Iterator<?> iterator = collection.iterator();
                            for (; iterator.hasNext(); ) {
                                Object next = iterator.next();
                                Object val = next;
                                if (grammar.getItemKey() != null) {
                                    val = DataUtils.getValueDeep(next, grammar.getItemKey());
                                }
                                Cell newCell = newCell(sheet, cell, x, y);
                                setCellValue(newCell, buildExp((ExcelTemplateForGrammar) val), grammar);
                                if ("x".equals(move)) {
                                    x++;
                                } else {
                                    y++;
                                }
                            }
                            scannerForCode(sheet, data, move);
                        }
                    }
                }
            }

        }
    }

    ;

    /**
     * 扫描
     *
     * @param data
     */
    private void scannerText(Sheet sheet, Map<String, Object> data) {
        //最大行数
        int lastRowNum = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            //获取行
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                //获取本行最大单元格数
                short lastCellNum = row.getLastCellNum();
                for (short cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //读取
                    if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                        ExcelTemplateGrammar analyse = analyse(cell.getStringCellValue(), ExcelTemplateGrammarEnum.TEXT);
                        if (analyse != null) {
                            //获取数据
                            Object value = DataUtils.getValueDeep(data, analyse.getKey());
                            setCellValue(cell, value, analyse);
                        }
                    }
                }
            }

        }
    }

    /**
     * 扫描
     *
     * @param data
     */
    private void scannerFor(Sheet sheet, Map<String, Object> data, String move) {
        scannerFor(sheet, data, move, new ArrayList<>());
    }

    /**
     * 扫描
     *
     * @param data
     */
    private void scannerFor(Sheet sheet, Map<String, Object> data, String move, List<List<Integer>> expand) {
        //最大行数
        int lastRowNum = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            //获取行
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                //获取本行最大单元格数
                short lastCellNum = row.getLastCellNum();
                for (short cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //读取
                    if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                        ExcelTemplateGrammar analyse = analyse(cell.getStringCellValue(),
                                "x".equals(move)
                                        ? ExcelTemplateGrammarEnum.FOR_X
                                        : ExcelTemplateGrammarEnum.FOR_Y
                        );
                        if (analyse != null) {
                            ExcelTemplateForGrammar grammar = (ExcelTemplateForGrammar) analyse;
                            int x = cellIndex, y = rowIndex;
                            //获取数组对象
                            String key = grammar.getKey();
                            Collection<?> collection = (Collection<?>) DataUtils.getValue(data, key);
                            collection = collection == null ? new ArrayList<>() : collection;
                            //移动行，来实现插入的功能
                            if (ExcelTemplateGrammarEnum.FOR_Y == grammar.getGrammar()) {
                                expand(x, y, collection.size(), expand, sheet);
                            }
                            cell.setCellValue("");
                            for (int i = 0; i < collection.size(); i++) {
                                Cell newCell = newCell(sheet, cell, x, y);
                                setCellValue(newCell, buildExp(i, grammar), grammar);
                                if ("x".equals(move)) {
                                    x++;
                                } else {
                                    y++;
                                }
                            }
                            scannerFor(sheet, data, move, expand);
                        }
                    }
                }
            }

        }
    }

    /**
     * 扩张行
     *
     * @param x
     * @param y
     * @param len
     * @param expand
     */
    private void expand(int x, int y, int len, List<List<Integer>> expand, Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        List<Integer> arr = null;
        for (List<Integer> es : expand) {
            int start = es.get(0);
            int end = es.get(1);
            if (y <= end && y >= start) {
                arr = es;
                break;
            }
        }
        if (arr == null) {
            sheet.shiftRows(y + 1, lastRowNum, len - 1);
            arr = new ArrayList<>();
            arr.add(y);
            arr.add(y + len - 1);
            expand.add(arr);
        } else {
            Integer start = arr.get(0);
            Integer end = arr.get(1);
            int newRowLen = len - (end - y) - 1;
            sheet.shiftRows(end + 1, lastRowNum, newRowLen);
            arr.set(1, end + newRowLen);
        }
    }

    /**
     * 创建一个新的单元格,如果该单元格不存在，则使用传入的单元格样式创建一个单元格
     *
     * @return
     */
    private Cell newCell(Sheet sheet, Cell cell, int x, int y) {
        Row row = sheet.getRow(y);
        if (row == null) {
            row = sheet.createRow(y);
        }
        Cell cell1 = row.getCell(x);
        if (cell1 == null) {
            cell1 = row.createCell(x);
        }
        cell1.setCellStyle(cell.getCellStyle());
        return cell1;
    }

    /**
     * 生成表达式
     *
     * @param index
     * @param grammar
     * @return
     */
    private String buildExp(int index, ExcelTemplateForGrammar grammar) {
        StringBuilder sb = new StringBuilder();
        sb.append("$")
                .append("{")
                .append(grammar.getKey())
                .append("[")
                .append(index)
                .append("]");

        if (grammar.getItemKey() != null) {
            sb.append(".")
                    .append(grammar.getItemKey());
        }
        Map<String, Object> attr = new HashMap<>(8);
        attr.put("dateFormat", grammar.getDateFormat());
        sb.append(JSON.toJSONString(attr));
        sb.append("}");
        return sb.toString();
    }

    /**
     * 生成表达式
     *
     * @param val
     * @return
     */
    private String buildExp(ExcelTemplateForGrammar val) {
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        sb.append(val.getGrammar() == ExcelTemplateGrammarEnum.FOR_X ? "for-x" : "for-y");
        sb.append("{")
                .append(val.getKey());

        if (val.getItemKey() != null) {
            sb.append("<")
                    .append(val.getItemKey())
                    .append(">");
        }
        Map<String, Object> attr = new HashMap<>(8);
        attr.put("dateFormat", val.getDateFormat());
        sb.append(JSON.toJSONString(attr));
        sb.append("}");
        return sb.toString();
    }

    /**
     * 单元格赋值
     *
     * @param cell
     * @param o
     */
    private void setCellValue(Cell cell, Object o, ExcelTemplateGrammar grammar) {
        String value = cell.getStringCellValue();
        String text = objectToText(o, grammar);
        ExcelTemplateGrammarEnum[] values = ExcelTemplateGrammarEnum.values();
        value = value.replaceAll(grammar.getGrammar().getMatch().pattern(), text.replaceAll("\\$", "\\\\\\$"));
        if (StringUtils.isEmpty(value)) {
            value = text;
        }
        cell.setCellValue(value);
    }

    private String objectToText(Object o, ExcelTemplateGrammar grammar) {
        String text = null;
        if (o == null) {
            return "--";
        }
        if (o instanceof Date) {
            if (grammar.getDateFormat() != null) {
                DateFormat dateFormat = TimeUtils.getDateFormat(grammar.getDateFormat());
                text = dateFormat.format(o);
            }
        } else {
            text = String.valueOf(o);
        }

        return text;
    }

    /**
     * 分析内容
     *
     * @param text
     * @return
     */
    private ExcelTemplateGrammar analyse(String text, ExcelTemplateGrammarEnum grammarEnum) {
        switch (grammarEnum) {
            case TEXT:
                Matcher matcherText = ExcelTemplateGrammarEnum.TEXT.getFind().matcher(text);
                boolean isText = matcherText.find();
                if (isText) {
                    ExcelTemplateGrammar grammar = new ExcelTemplateGrammar();
                    grammar.setGrammar(ExcelTemplateGrammarEnum.TEXT);
                    String group = matcherText.group();
                    Matcher matcher = ExcelTemplateGrammar.ATTR_PATTERN.matcher(group);
                    grammar.setKey(group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), ""));
                    if (matcher.find()) {
                        String attr = matcher.group();
                        JSONObject jsonObject = JSONObject.parseObject(attr);
                        String dateFormat = jsonObject.getString("dateFormat");
                        grammar.setDateFormat(dateFormat);
                    }
                    return grammar;
                }
                break;
            case FOR_X:
                Matcher matcherForX = ExcelTemplateGrammarEnum.FOR_X.getFind().matcher(text);
                boolean isForX = matcherForX.find();
                if (isForX) {
                    ExcelTemplateForGrammar grammar = new ExcelTemplateForGrammar();
                    grammar.setGrammar(ExcelTemplateGrammarEnum.FOR_X);
                    String group = matcherForX.group();
                    grammar.setKey(group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), "")
                            .replaceAll("<.+>", ""));
                    Matcher matcher = ExcelTemplateGrammar.ATTR_PATTERN.matcher(group);
                    if (matcher.find()) {
                        String attr = matcher.group();
                        extractAttr(grammar, attr);
                    }
                    if (ExcelTemplateForGrammar.LIST_ITEM_KEY_PATTERN.matcher(group).find()) {
                        grammar.setItemKey(
                                group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), "")
                                        .replaceAll(".+(?=<.+>)", "")
                                        .replaceAll("[<>]", "")
                        );
                    }
                    return grammar;
                }
                break;
            case FOR_Y:
                Matcher matcherForY = ExcelTemplateGrammarEnum.FOR_Y.getFind().matcher(text);
                boolean isForY = matcherForY.find();
                if (isForY) {
                    ExcelTemplateForGrammar grammar = new ExcelTemplateForGrammar();
                    grammar.setGrammar(ExcelTemplateGrammarEnum.FOR_Y);
                    String group = matcherForY.group();
                    grammar.setKey(
                            group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), "").replaceAll("<.+>", "")
                    );
                    if (ExcelTemplateForGrammar.LIST_ITEM_KEY_PATTERN.matcher(group).find()) {
                        grammar.setItemKey(
                                group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), "")
                                        .replaceAll(".+(?=<.+>)", "")
                                        .replaceAll("[<>]", "")
                        );
                    }
                    return grammar;
                }
                break;
            case FOR_CODE_X:
                Matcher matcherForCodeX = ExcelTemplateGrammarEnum.FOR_CODE_X.getFind().matcher(text);
                boolean isForCodeX = matcherForCodeX.find();
                if (isForCodeX) {
                    ExcelTemplateForCodeGrammar grammar = new ExcelTemplateForCodeGrammar();
                    grammar.setGrammar(ExcelTemplateGrammarEnum.FOR_CODE_X);
                    String group = matcherForCodeX.group();
                    grammar.setKey(
                            group.replaceAll(ExcelTemplateGrammar.ATTR_PATTERN.pattern(), "")
                                    .replaceAll("<.+>", "")
                    );
                    if (ExcelTemplateForGrammar.LIST_ITEM_KEY_PATTERN.matcher(group).find()) {
                        grammar.setItemKey(
                                group.replaceAll(".+(?=<.+>)", "")
                                        .replaceAll("[<>]", "")
                        );
                    }
                    return grammar;
                }
                break;
            case FOR_CODE_Y:
                Matcher matcherForCodeY = ExcelTemplateGrammarEnum.FOR_CODE_Y.getFind().matcher(text);
                boolean isForCodeY = matcherForCodeY.find();
                if (isForCodeY) {
                    ExcelTemplateForCodeGrammar grammar = new ExcelTemplateForCodeGrammar();
                    grammar.setGrammar(ExcelTemplateGrammarEnum.FOR_CODE_Y);
                    String group = matcherForCodeY.group();
                    grammar.setKey(
                            group.replaceAll("<.+>", "")
                    );
                    if (ExcelTemplateForGrammar.LIST_ITEM_KEY_PATTERN.matcher(group).find()) {
                        grammar.setItemKey(
                                group.replaceAll(".+(?=<.+>)", "")
                                        .replaceAll("[<>]", "")
                        );
                    }
                    return grammar;
                }
                break;
            default:
                return null;
        }
        return null;
    }

    /**
     * 提取参数
     *
     * @param grammar
     * @param attrStr
     */
    private void extractAttr(ExcelTemplateGrammar grammar, String attrStr) {
        JSONObject jsonObject = JSONObject.parseObject(attrStr);
        for (String key : jsonObject.keySet()) {
            DataUtils.setValue(grammar, key, jsonObject.get(key));
        }
    }

}
