package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author fenqing
 */
@Data
@ExcelData(type = ExcelDataTypeEnum.OBJECT, sheet = 0)
public class BaseDataDto {

    @ExcelField(coordinate = "C3")
    private String productName;

    @ExcelField(coordinate = "H3")
    private String samplingPlan;

    @ExcelField(coordinate = "k3")
    private String edition;

    @ExcelField(coordinate = "C3")
    private String filed4;

    @ExcelField(coordinate = "H4")
    private String filed5;

    @ExcelField(coordinate = "K4")
    private String filed6;

    @ExcelField(coordinate = "H5")
    private String filed7;

    @ExcelField(coordinate = "K5")
    private String filed8;

    @ExcelField(coordinate = "A5", img = true)
    private List<String> img;


}
