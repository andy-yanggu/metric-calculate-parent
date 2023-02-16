package com.yanggu.metric_calculate.core.middle_store;

import com.yanggu.metric_calculate.core.cube.MetricCube;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 派生指标中间结算结果存储接口
 * 可以实现远端存储和本地存储
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
    void update(MetricCube cube);

    /**
     * 批量查询, 默认实现是for循环调用get
     * <p>如果外部存储支持批量查询, 可以重写该方法</p>
     *
     * @param cubeList
     * @return
     */
    default List<MetricCube> batchGet(List<MetricCube> cubeList) {
        return cubeList.stream().map(this::get).collect(Collectors.toList());
    }

    /**
     * 批量更新, 默认实现是for循环调用update
     * <p>如果外部存储支持批量更新, 可以重写该方法</p>
     *
     * @param cubeList
     */
    default void batchUpdate(List<MetricCube> cubeList) {
        cubeList.forEach(this::update);
    }

}
