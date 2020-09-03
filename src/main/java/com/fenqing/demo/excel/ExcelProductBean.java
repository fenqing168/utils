package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelSheet;
import lombok.Data;

import java.util.List;

/**
 * @author fenqing
 */
@Data
@ExcelSheet(sheet = 0)
public class ExcelProductBean {

    private ExcelProductBaseBean productBase;

    private List<ExcelProdChkParamBean> prodChkParams;

    private List<ExcelProductPropertyBean> productProperties;

    private List<ExcelProdExtInfoBean> prodExtInfos;

}
