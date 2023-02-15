package com.yanggu.metric_calculate.core.annotation;

import com.yanggu.metric_calculate.core.enums.TimeWindowEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SPLIT_WINDOW;

/**
 * 聚合函数元数据
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeType {

    /**
     * 名称, 唯一标识
     *
     * @return
     */
    String value();

    /**
     * 描述信息
     * @return
     */
    String description() default "";

    /**
     * 是否使用参数
     *
     * @return
     */
    boolean useParam() default false;

    /**
     * 是否使用子聚合函数
     *
     * @return
     */
    boolean useExternalAgg() default false;

    /**
     * 时间区间类型
     *
     * @return
     */
    TimeWindowEnum timeWindowType() default TIME_SPLIT_WINDOW;

}
