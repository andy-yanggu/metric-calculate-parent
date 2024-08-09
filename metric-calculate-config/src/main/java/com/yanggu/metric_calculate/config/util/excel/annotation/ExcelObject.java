package com.yanggu.metric_calculate.config.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelObject {

    /**
     * 名称
     *
     * @return
     */
    String name();

    /**
     * 不需要导出的字段
     * <p>默认为空, 全部导出</p>
     *
     * @return
     */
    String[] excludeNameArray() default {};

}
