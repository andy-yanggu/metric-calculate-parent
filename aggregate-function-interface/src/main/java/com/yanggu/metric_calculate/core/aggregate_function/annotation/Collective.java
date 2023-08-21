package com.yanggu.metric_calculate.core.aggregate_function.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 集合型
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collective {

    /**
     * 主键策略
     * <p>0没有主键</p>
     * <p>1去重字段</p>
     * <p>2排序字段</p>
     * <p>3比较字段</p>
     *
     * @return 0、1、2
     */
    int keyStrategy() default 0;

    /**
     * 保留字段策略
     * <p>0不保留任何数据</p>
     * <p>1保留指定字段</p>
     * <p>2保留原始数据</p>
     *
     * @return 0、1、2
     */
    int retainStrategy() default 0;

}
