package com.yanggu.metriccalculate.calculate;

/**
 * 原子指标计算、派生指标计算、复合指标计算接口
 * @param <E> 输入数据
 * @param <R> 输出数据
 */
public interface Calculate<E, R> {

    /**
     * 初始化方法
     */
    default void init(TaskContext taskContext) throws RuntimeException {
        throw new RuntimeException("需要重写init方法");
    }

    /**
     * 计算方法
     */
    R exec(E e) throws Exception;

    /**
     * 保存到外部存储
     */
    default void save(Object result) throws RuntimeException {
        throw new RuntimeException("需要重写save方法");
    }

}
