package com.yanggu.metric_calculate.core.annotation;

import java.lang.annotation.*;

/**
 * 对象型
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Objective {

    /**
     * 是否需要指定比较参数
     *
     * @return true需要配置比较字段, false不需要配置比较字段
     */
    boolean useCompareField() default true;

    /**
     * 是否使用排序字段
     *
     * @return
     */
    boolean useSortedField() default false;

    /**
     * 是否保留对象
     *
     * @return true保留对象, false保留字段
     */
    boolean retainObject() default true;

}
