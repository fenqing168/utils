package com.fenqing.excel.bean;

import com.fenqing.excel.enumeration.ExcelTemplateGrammarEnum;
import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author fenqing
 */
@Data
public class ExcelTemplateForGrammar extends ExcelTemplateGrammar{

    public static Pattern LIST_ITEM_KEY_PATTERN = Pattern.compile("<.+>");

    private String itemKey;

    public static ExcelTemplateForGrammar getInstance(ExcelTemplateGrammarEnum grammar, String key, String itemKey){
        ExcelTemplateForGrammar excelTemplateForGrammar = new ExcelTemplateForGrammar();
        excelTemplateForGrammar.setKey(key);
        excelTemplateForGrammar.setGrammar(grammar);
        excelTemplateForGrammar.setItemKey(itemKey);
        return excelTemplateForGrammar;
    }
}
