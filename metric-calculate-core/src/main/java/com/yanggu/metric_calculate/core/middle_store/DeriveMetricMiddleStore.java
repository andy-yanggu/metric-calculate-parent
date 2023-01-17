package com.yanggu.metric_calculate.core.middle_store;

import com.yanggu.metric_calculate.core.cube.MetricCube;

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
     * @return
     */
    MetricCube get(MetricCube cube);

    /**
     * 通过key进行更新
     *
     * @param cube
     */
    void put(MetricCube cube);

}
