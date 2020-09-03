package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import lombok.Data;

/**
 * @author fenqing
 */
@ExcelData(sheet = 0, type = ExcelDataTypeEnum.OBJECT)
@Data
public class ExcelProductBaseBean {

    @ExcelField(coordinate = "C3")
    private String name;

    @ExcelField(coordinate = "F3")
    private String hyEdition;

    @ExcelField(coordinate = "C4")
    private String partNumber;

    @ExcelField(coordinate = "F4")
    private String clientEdition;

    @ExcelField(coordinate = "F5")
    private String sizeEdition;

}
