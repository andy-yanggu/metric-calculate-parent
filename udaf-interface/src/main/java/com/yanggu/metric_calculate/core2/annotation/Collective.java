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
     * 保留字段策略
     * <p>0不保留任何数据</p>
     * <p>1保留指定字段</p>
     * <p>2保留原始数据</p>
     *
     * @return
     */
    int retainStrategy() default 0;

}
