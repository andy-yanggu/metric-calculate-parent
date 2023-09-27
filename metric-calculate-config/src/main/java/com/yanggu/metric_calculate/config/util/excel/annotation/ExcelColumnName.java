package com.yanggu.metric_calculate.config.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumnName {

    /**
     * 导出到Excel中的名字.
     */
    String name() default "";

    /**
     * 导出时在excel中排序.
     */
    int sort() default Integer.MAX_VALUE;

}
