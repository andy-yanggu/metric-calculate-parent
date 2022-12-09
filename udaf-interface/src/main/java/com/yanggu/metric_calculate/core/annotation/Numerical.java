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
}
