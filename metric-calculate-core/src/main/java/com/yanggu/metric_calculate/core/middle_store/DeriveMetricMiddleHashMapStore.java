package com.yanggu.metric_calculate.core.middle_store;


import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class DeriveMetricMiddleHashMapStore implements DeriveMetricMiddleStore {

    private KryoPool kryoPool;

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
    public void update(MetricCube cube) {
        localMap.put(cube.getRealKey(), cube);
    }

}
