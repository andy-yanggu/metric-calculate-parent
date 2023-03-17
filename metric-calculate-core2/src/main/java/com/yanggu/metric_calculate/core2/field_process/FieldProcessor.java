package com.yanggu.metric_calculate.core2.field_process;

/**
 * 字段处理器, 从输入的数据中, 根据相关的配置, 提取出相关的数据
 *
 * @param <T> 输入数据
 * @param <R> 从输入数据中提取的数据
 */
public interface FieldProcessor<T, R> {

    /**
     * 初始化方法
     *
     * @throws Exception
     */
    void init() throws Exception;

    R process(T input) throws Exception;

}
