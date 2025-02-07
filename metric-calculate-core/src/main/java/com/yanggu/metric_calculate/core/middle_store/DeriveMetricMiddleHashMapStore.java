package com.yanggu.metric_calculate.core.middle_store;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DeriveMetricMiddleHashMapStore extends AbstractDeriveMetricMiddleStore {

    private Map<DimensionSet, MetricCube> localMap;

    @Override
    public void init() {
        localMap = new ConcurrentHashMap<>();
    }

    @Override
    public <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet) {
        return localMap.get(dimensionSet);
    }

    @Override
    public <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube) {
        localMap.put(updateMetricCube.getDimensionSet(), updateMetricCube);
    }

    @Override
    public void deleteData(DimensionSet dimensionSet) {
        localMap.remove(dimensionSet);
    }

}
