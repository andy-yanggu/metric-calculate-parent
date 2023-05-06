package com.yanggu.metric_calculate.core2.calculate;

/**
 * 字段计算类
 */
public interface FieldCalculate<T, R> {

    /**
     * 初始化方法
     */
    default void init() {
    }

    R process(T input) throws Exception;

}
