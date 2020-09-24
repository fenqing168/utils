package com.fenqing.excel.bean;

import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author fenqing
 */
@Data
public class ExcelTemplateGrammar {

    public static Pattern ATTR_PATTERN = Pattern.compile("\\{.*\\}");;

    private ExcelTemplateGrammarEnum grammar;

    private String key;

    private String dateFormat;

}
