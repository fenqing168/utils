package com.fenqing.excel.enumeration;

import java.util.regex.Pattern;

/**
 * 语法类别
 * @author fenqing
 */
public enum  ExcelTemplateGrammarEnum {
    /**
     * 纯文本
     */
    TEXT(Pattern.compile("(?<=\\$\\{).+(?=(\\[.+])?})"), Pattern.compile("\\$\\{.+(\\[.+])?}")),
    /**
     * 纵向循环
     */
    FOR_Y(Pattern.compile("(?<=\\$for-y\\{).+(?=(\\[.+])?})"), Pattern.compile("\\$for-y\\{.+(\\[.+])?}")),
    /**
     * 横向循环
     */
    FOR_X(Pattern.compile("(?<=\\$for-x\\{).+(?=(\\[.+])?})"), Pattern.compile("\\$for-x\\{.+(\\[.+])?}")),
    /**
     * 纵向循环生成代码
     */
    FOR_CODE_Y(Pattern.compile("(?<=\\$for-code-y\\{).+(?=})"), Pattern.compile("\\$for-x\\{.+}")),
    /**
     * 横向循环生成代码
     */
    FOR_CODE_X(Pattern.compile("(?<=\\$for-code-x\\{).+(?=})"), Pattern.compile("\\$for-x\\{.+}"));

    private Pattern find;

    private Pattern match;

    ExcelTemplateGrammarEnum(Pattern find, Pattern match) {
        this.find = find;
        this.match = match;
    }

    public Pattern getFind() {
        return find;
    }

    public Pattern getMatch() {
        return match;
    }
}
