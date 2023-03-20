package com.yanggu.metric_calculate.core2.middle_store;


import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DeriveMetricMiddleHashMapKryoStore extends AbstractDeriveMetricMiddleStore {

    private Map<DimensionSet, byte[]> localMap;

    @Override
    public void init() {
        localMap = new ConcurrentHashMap<>();
    }

    @Override
    public <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet) {
        return super.deserialize(localMap.get(dimensionSet));
    }

    @Override
    public <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube) {
        localMap.put(updateMetricCube.getDimensionSet(), super.serialize(updateMetricCube));
    }

}
