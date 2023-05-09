package com.yanggu.metric_calculate.core2.middle_store;


import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.yanggu.metric_calculate.core2.middle_store.AbstractDeriveMetricMiddleStore.DeriveMetricMiddleStoreHolder.DEFAULT_IMPL;
import static com.yanggu.metric_calculate.core2.middle_store.AbstractDeriveMetricMiddleStore.DeriveMetricMiddleStoreHolder.STORE_MAP;


public class DeriveMetricMiddleHashMapStore extends AbstractDeriveMetricMiddleStore {

    private Map<DimensionSet, MetricCube> localMap;

    @Override
    public void init() {
        localMap = new ConcurrentHashMap<>();
        //默认放入memory
        STORE_MAP.put(DEFAULT_IMPL, this);
    }

    @Override
    public <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet) {
        return localMap.get(dimensionSet);
    }

    @Override
    public <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube) {
        localMap.put(updateMetricCube.getDimensionSet(), updateMetricCube);
    }

}
