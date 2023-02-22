package com.yanggu.metric_calculate.core.annotation;

import java.lang.annotation.*;

/**
 * 数值型
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Objective
public @interface Numerical {

    /**
     * 是否需要多个参数
     * <p>主要是协方差需要多个参数</p>
     *
     * @return
     */
    boolean multiNumber() default false;

}
