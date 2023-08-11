package com.yanggu.metric_calculate.core2.middle_store;

import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 派生指标中间结算结果存储接口
 * <p>可以实现远端存储和本地存储</p>
 */
public interface DeriveMetricMiddleStore {

    /**
     * 初始化方法<br>
     * 需要将初始化完成后的实现类放入map中
     */
    void init();

    /**
     * 通过维度查询
     *
     * @return
     */
    <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet);

    /**
     * 通过维度进行更新
     *
     * @param updateMetricCube
     */
    <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube);

    /**
     * 批量查询, 默认实现是for循环调用get
     * <p>如果外部存储支持批量查询, 可以重写该方法</p>
     *
     * @param dimensionSetList
     * @return
     */
    default Map<DimensionSet, MetricCube> batchGet(List<DimensionSet> dimensionSetList) {
        Map<DimensionSet, MetricCube> map = new HashMap<>();
        for (DimensionSet dimensionSet : dimensionSetList) {
            MetricCube historyMetricCube = get(dimensionSet);
            if (historyMetricCube != null) {
                map.put(dimensionSet, historyMetricCube);
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

    /**
     * 根据维度删除数据
     *
     * @param dimensionSet
     */
    void deleteData(DimensionSet dimensionSet);

    /**
     * 批量删除
     *
     * @param dimensionSetList
     */
    default void batchDeleteData(List<DimensionSet> dimensionSetList) {
        dimensionSetList.forEach(this::deleteData);
    }

}
