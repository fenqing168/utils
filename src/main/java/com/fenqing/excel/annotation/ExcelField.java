package com.fenqing.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个表里的字段
 * @author fenqing
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 顺序
     * @return
     */
    int sort();

    /**
     * 是否为图片
     * @return
     */
    boolean isImgs() default false;

}
