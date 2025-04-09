package com.yanggu.metric_calculate.test.junit5.param;

import java.lang.annotation.*;

/**
 * json参数注解
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonParam {

    /**
     * json取值路径
     */
    String value() default "";

    /**
     * 是否必须有值
     */
    boolean required() default true;

    /**
     * 默认值
     */
    String defaultValue() default "";

}