package com.yanggu.metric_calculate.core.middle_store;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 派生指标中间结算结果存储接口
 * 可以实现远端存储和本地存储
 */
public interface DeriveMetricMiddleStore<M extends MergedUnit<M> & Value<?>> {

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
    MetricCube<Table, Long, M, ?> get(MetricCube<Table, Long, M, ?> cube);

    /**
     * 通过key进行更新
     *
     * @param cube
     */
    void update(MetricCube<Table, Long, M, ?> cube);

    /**
     * 批量查询, 默认实现是for循环调用get
     * <p>如果外部存储支持批量查询, 可以重写该方法</p>
     *
     * @param cubeList
     * @return
     */
    default Map<DimensionSet, MetricCube<Table, Long, ?, ?>> batchGet(List<MetricCube<Table, Long, M, ?>> cubeList) {
        Map<DimensionSet, MetricCube<Table, Long, ?, ?>> map = new HashMap<>();
        for (MetricCube<Table, Long, M, ?> metricCube : cubeList) {
            MetricCube<Table, Long, M, ?> historyMetricCube = get(metricCube);
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
    default void batchUpdate(List<MetricCube<Table, Long, M, ?>> cubeList) {
        cubeList.forEach(this::update);
    }

}
