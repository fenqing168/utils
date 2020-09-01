package com.fenqing.excel.enumeration;

/**
 * @author fenqing
 */
public enum  ExcelFileTypeEnum {
    /**
     * xls类型
     */
    XLS("xls"),
    /**
     * xlsx类型
     */
    XLSX("xlsx");

    ExcelFileTypeEnum(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public static ExcelFileTypeEnum findType(String type){
        ExcelFileTypeEnum[] values = ExcelFileTypeEnum.values();
        for (ExcelFileTypeEnum value : values) {
            if(value.type.equalsIgnoreCase(type)){
                return value;
            }
        }
        return XLS;
    }

}
