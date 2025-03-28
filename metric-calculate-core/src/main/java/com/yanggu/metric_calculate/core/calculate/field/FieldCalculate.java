package com.yanggu.metric_calculate.core.calculate.field;

import java.util.Set;

/**
 * 字段计算类
 */
public interface FieldCalculate<T, R> {

    /**
     * 初始化方法
     */
    default void init() {
    }

    /**
     * 依赖的字段。默认为空
     */
    default Set<String> dependFields() {
        return Set.of();
    }

    /**
     * 获取字段名称
     *
     * @return
     */
    String getName();

    /**
     * 执行字段计算
     *
     * @param input
     * @return
     * @throws Exception
     */
    R process(T input) throws Exception;

}
