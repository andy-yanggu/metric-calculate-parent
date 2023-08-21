package com.yanggu.metric_calculate.core.aggregate_function.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 聚合函数元数据
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeType {

    /**
     * 名称, 唯一标识
     *
     * @return
     */
    String value();

    /**
     * 描述信息
     *
     * @return
     */
    String description() default "";

}
