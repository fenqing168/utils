package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.annotation.ExcelTable;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author fenqing
 */
@Data
@ExcelData(type = ExcelDataTypeEnum.LIST, sheet = 0)
@ExcelTable(startText = "参数检验设置", endText = "属性检验设置")
public class ParamsDto {

    private Integer no;

    @ExcelField(x = "B")
    private String field1;

//    @ExcelField(x = "C")
//    private String field2;

    @ExcelField(x = "D")
    private String field3;

    @ExcelField(x = "E")
    private String field4;

    @ExcelField(x = "F")
    private String field5;

    @ExcelField(x = "G")
    private String field6;

    @ExcelField(x = "H")
    private String field7;

    @ExcelField(x = "I")
    private String field8;

    @ExcelField(x = "J")
    private String field9;

    @ExcelField(x = "K")
    private String field10;

    @ExcelField(x = "L")
    private String field11;

    @ExcelField(x = "C", img = true)
    private List<String> imgs;
}
