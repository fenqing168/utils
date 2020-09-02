package com.fenqing.demo.excel;

import com.fenqing.excel.annotation.ExcelSheet;
import lombok.Data;

import java.util.List;

/**
 * @author fenqing
 */
@Data
@ExcelSheet(sheet = 0)
public class ProductImport {

    private BaseDataDto baseDataDto;

    private List<ParamsDto> paramsDto;
}
