package com.yanggu.metric_calculate.core.middle_store;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 派生指标中间结算结果存储接口
 * 可以实现远端存储和本地存储
 */
public interface DeriveMetricMiddleStore {

    /**
     * 初始化方法<br>
     * 需要将初始化完成后的实现类放入map中
     */
    void init();

    default void setKryoPool(KryoPool kryoPool) {
    }

    /**
     * 通过key查询
     *
     * @return
     */
    MetricCube get(MetricCube queryMetricCube);

    /**
     * 通过key进行更新
     *
     * @param updateMetricCube
     */
    void update(MetricCube updateMetricCube);

    /**
     * 批量查询, 默认实现是for循环调用get
     * <p>如果外部存储支持批量查询, 可以重写该方法</p>
     *
     * @param cubeList
     * @return
     */
    default Map<DimensionSet, MetricCube> batchGet(List<MetricCube> cubeList) {
        Map<DimensionSet, MetricCube> map = new HashMap<>();
        for (MetricCube metricCube : cubeList) {
            MetricCube historyMetricCube = get(metricCube);
            if (historyMetricCube != null) {
                map.put(metricCube.getDimensionSet(), historyMetricCube);
            }
        }
        return map;
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
