package com.yanggu.metric_calculate.test.junit5.param;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义json数组文件路径。在resource路径下
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonArgumentsProvider.class)
public @interface JsonSource {

    /**
     * json文件路径
     */
    String value();

    /**
     * 标识只有一个参数时，是平铺还是聚合对象
     */
    boolean aggregate() default true;

}