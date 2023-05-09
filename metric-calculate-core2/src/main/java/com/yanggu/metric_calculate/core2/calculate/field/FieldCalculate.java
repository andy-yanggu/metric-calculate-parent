package com.yanggu.metric_calculate.core2.calculate.field;

/**
 * 字段计算类
 */
public interface FieldCalculate<T, R> {

    /**
     * 初始化方法
     */
    default void init() {
    }

    String getName();

    R process(T input) throws Exception;

}
