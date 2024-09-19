package com.yanggu.metric_calculate.config.util.excel.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于不同的校验注解 校验Excel单元格数据
 *
 * @author yy
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelValid {

    BaseValid[] valid();

    String message() default ""; // 错误信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
