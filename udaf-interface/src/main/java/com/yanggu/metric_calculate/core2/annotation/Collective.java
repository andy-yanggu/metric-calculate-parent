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
     * 主键策略
     * <p>0不使用任何字段作为主键</p>
     * <p>1去重字段</p>
     * <p>2排序字段</p>
     * <p>3比较字段</p>
     * @return
     */
    int keyStrategy() default 0;

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
