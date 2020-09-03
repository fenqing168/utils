package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.annotation.ExcelTable;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import lombok.Data;

/**
 * 用于excel表格导入
 * @author fenqing
 */
@ExcelTable(startText = "参数检验项目设置", endText = "属性检验项目设置")
@ExcelData(sheet = 0, type = ExcelDataTypeEnum.LIST)
@Data
public class ExcelProdChkParamBean {

    /**
     * 序号
     */
    private Integer no;

    /**
     * 图号
     */
    @ExcelField(x = "B")
    private String figureNumber;

    /**
     * 符号
     */
    @ExcelField(x = "C")
    private String symbol;

    /**
     * 检验项目
     */
    @ExcelField(x = "D")
    private String inspectionName;


    /**
     * 上公差
     */
    @ExcelField(x = "E")
    private String upperDeviation;

    /**
     * 标准值
     */
    @ExcelField(x = "F")
    private String standardValues;

    /**
     * 下公差
     */
    @ExcelField(x = "G")
    private String lowerDeviation;



    /**
     * 检查工具
     */
    @ExcelField(x = "H")
    private String inspectionTool;

}
