package com.yanggu.metric_calculate.core2.annotation;

import java.lang.annotation.*;

/**
 * 集合型
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collective {

    /**
     * 是否使用去重字段
     *
     * @return
     */
    boolean useDistinctField() default false;

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
