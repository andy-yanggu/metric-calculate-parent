package com.yanggu.metric_calculate.core2.aviator_function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Aviator函数名称注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AviatorFunctionName {

    /**
     * 名称, 唯一标识
     *
     * @return
     */
    String value();

}
