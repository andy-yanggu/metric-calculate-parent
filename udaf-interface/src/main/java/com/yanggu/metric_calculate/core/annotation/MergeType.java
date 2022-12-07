package com.yanggu.metric_calculate.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeType {

    /**
     * 名称, 唯一标识
     *
     * @return
     */
    String value();

}
