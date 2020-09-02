package com.fenqing.excel.annotation;

import com.fenqing.excel.enumeration.ExcelDataTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * @author fenqing
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelData {

    ExcelDataTypeEnum type();

    int sheet() default 0;

}
