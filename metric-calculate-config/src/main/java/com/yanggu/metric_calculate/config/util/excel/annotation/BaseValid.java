package com.yanggu.metric_calculate.config.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseValid {

    Class<?> validClass(); // 校验类
    String message() default ""; // 错误信息

    // **************************** 长度校验参数******************************
    int min() default 1; // 最小长度
    int max() default Integer.MAX_VALUE; // 最大长度

    // **************************** 枚举类校验******************************

    /**
     * 枚举get方法名称
     */
    String methodName() default "getDesc";
}
