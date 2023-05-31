package com.yanggu.metric_calculate.core2.annotation;

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
     * 保留字段策略
     * <p>0不保留任何数据</p>
     * <p>1保留指定字段</p>
     * <p>2保留原始数据</p>
     *
     * @return
     */
    int retainStrategy() default 0;

}
