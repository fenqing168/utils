package com.fenqing.excel.enumeration;

/**
 * @author fenqing
 */
public enum ExcelDataTypeEnum {
    /**
     * object类型
     */
    OBJECT("Object"),
    /**
     * List类型
     */
    LIST("List");

    ExcelDataTypeEnum(String type) {
        this.type = type;
    }

    private String type;


}
