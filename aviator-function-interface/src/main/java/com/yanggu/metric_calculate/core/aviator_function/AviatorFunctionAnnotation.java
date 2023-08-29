package com.yanggu.metric_calculate.core.aviator_function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Aviator函数名称注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AviatorFunctionAnnotation {

    /**
     * 英文名, 唯一标识
     *
     * @return
     */
    String name();

    /**
     * 中文名, 唯一性
     *
     * @return
     */
    String displayName();

    /**
     * 描述信息
     *
     * @return
     */
    String description() default "";

}
