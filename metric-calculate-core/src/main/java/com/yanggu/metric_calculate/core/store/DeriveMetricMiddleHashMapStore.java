package com.yanggu.metric_calculate.core.store;


import com.yanggu.metric_calculate.core.cube.MetricCube;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeriveMetricMiddleHashMapStore implements DeriveMetricMiddleStore {

    private Map<String, MetricCube> localMap;

    @Override
    public void init() {
        localMap = new ConcurrentHashMap<>();
    }

    @Override
    public MetricCube get(MetricCube cube) {
        return localMap.get(cube.getRealKey());
    }

    @Override
    public void put(MetricCube cube) {
        localMap.put(cube.getRealKey(), cube);
    }

}
