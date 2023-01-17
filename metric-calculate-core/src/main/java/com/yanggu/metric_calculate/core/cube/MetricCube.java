package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

public interface MetricCube<T extends Table, K, V, C extends MetricCube<T, K, V, C>>
        extends Cube<C>, KeyReferable, MergedUnit<C>, TimeReferable {

    DimensionSet dimensions();

    TimeBaselineDimension baselineDimension();

    void put(K key, V value);

    T table();

    /**
     * @param from          开始时间戳
     * @param fromInclusive 是否包含开始
     * @param to            结束时间戳
     * @param toInclusive   是否包含开始
     * @return
     */
    Value query(K from, boolean fromInclusive, K to, boolean toInclusive);

    int eliminateExpiredData();

}