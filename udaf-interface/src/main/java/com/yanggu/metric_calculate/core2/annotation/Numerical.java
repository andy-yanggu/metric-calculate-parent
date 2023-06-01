package com.yanggu.metric_calculate.core2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数值型
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Numerical {

    /**
     * 是否需要多个参数
     * <p>主要是协方差需要多个参数</p>
     *
     * @return
     */
    boolean multiNumber() default false;

}
