package com.fenqing.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个表的范围
 * @author fenqing
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTable {

    /**
     * 表格开始标识
     * @return
     */
    String startText();

    /**
     * 表格开始标识
     * @return
     */
    String endText();

    /**
     * 宽度
     * @return
     */
    int width();

    /**
     * 靠左偏移值
     * @return
     */
    int leftStart() default 0;

}
