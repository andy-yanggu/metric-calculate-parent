package com.yanggu.metric_calculate.config.util.excel.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {

    Class<? extends Enum<?>> enumClass(); // 枚举类

    String message() default "不在枚举类范围内"; // 错误信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举get方法名称
     */
    String methodName() default "getDesc";
}
