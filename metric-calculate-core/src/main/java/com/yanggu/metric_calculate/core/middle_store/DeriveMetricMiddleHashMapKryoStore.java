package com.yanggu.metric_calculate.core.middle_store;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DeriveMetricMiddleHashMapKryoStore extends AbstractDeriveMetricMiddleStore {

    private Map<DimensionSet, byte[]> localMap;

    @Override
    public void init() {
        localMap = new ConcurrentHashMap<>();
    }

    @Override
    public MetricCube get(MetricCube cube) {
        return super.deserialize(localMap.get(cube.getDimensionSet()));
    }

    @Override
    public void update(MetricCube cube) {
        localMap.put(cube.getDimensionSet(), super.serialize(cube));
    }

}
