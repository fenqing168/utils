package com.fenqing.excel.bean;

import lombok.Data;

/**
 * 表格配置
 * @author fenqing
 */
@Data
public class TableConfig{
    private Class<?> clazz;
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
}