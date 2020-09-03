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
@ExcelData(sheet = 0, type = ExcelDataTypeEnum.LIST)
@ExcelTable(startText = "附件上传", endText = "结束")
public class ExcelProdExtInfoBean {

    /**
     * 检验项目
     */
    @ExcelField(x = "A")
    private String url;

    @ExcelField(x = "D")
    private String describe;

}
