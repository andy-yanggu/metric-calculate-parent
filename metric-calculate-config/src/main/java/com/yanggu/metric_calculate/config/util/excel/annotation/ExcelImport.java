package com.yanggu.metric_calculate.config.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解标注的字段用于生成导入模板.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelImport {

    /**
     * 导出到Excel中的名字.
     */
    String name() default "";

    String enumDescKey() default "valueOf";

    /**
     * 下拉框选项.
     */
    String[] options() default {};

    String[] excludes() default {};
}
