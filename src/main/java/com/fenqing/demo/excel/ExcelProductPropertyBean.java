package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelData;
import com.fenqing.excel.annotation.ExcelField;
import com.fenqing.excel.annotation.ExcelTable;
import com.fenqing.excel.enumeration.ExcelDataTypeEnum;
import lombok.Data;

/**
 * @author fenqing
 */
@Data
@ExcelTable(startText = "属性检验项目设置", endText = "附件上传")
@ExcelData(sheet = 0, type = ExcelDataTypeEnum.LIST)
public class ExcelProductPropertyBean {

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
    private String inspectionStandard;

    /**
     * 检查工具
     */
    @ExcelField(x = "H")
    private String inspectionTool;

}
