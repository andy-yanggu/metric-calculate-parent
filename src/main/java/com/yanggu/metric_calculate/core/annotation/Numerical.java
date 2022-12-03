package com.yanggu.metric_calculate.core.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Objective
public @interface Numerical {
}
