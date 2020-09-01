package com.fenqing.excel.bean;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * 字段配置
 * @author fenqing
 */
@Data
public class FieldConfig{
    private Field field;
    private boolean isImgs;
}