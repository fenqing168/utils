package com.fenqing.excel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author fenqing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelTemplateForCodeGrammar extends ExcelTemplateForGrammar{

    private String itemKey;

}
