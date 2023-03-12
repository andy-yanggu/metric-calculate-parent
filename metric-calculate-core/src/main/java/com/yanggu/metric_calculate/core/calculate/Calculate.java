package com.yanggu.metric_calculate.core.calculate;

/**
 * 派生指标计算、复合指标计算接口
 *
 * @param <E> 输入数据
 * @param <R> 输出数据
 */
public interface Calculate<E, R> {

    /**
     * 初始化方法
     */
    default void init() throws RuntimeException {
    }

    /**
     * 计算方法
     */
    R exec(E e) throws Exception;

}
