package com.yanggu.metric_calculate.core.cube;

/**
 * 派生指标中间结算结果存储接口
 */
public interface DeriveMetricMiddleStore {

    /**
     * 初始化方法
     */
    default void init() {
        throw new RuntimeException("需要手动实现init方法");
    }

    /**
     * 通过key查询
     *
     * @param realKey
     * @return
     */
    MetricCube get(String realKey);

    /**
     * 通过key进行更新
     *
     * @param realKey
     * @param cube
     */
    void put(String realKey, MetricCube cube);

}
