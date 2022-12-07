package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

public interface MetricCube<T extends Table, K, V, C extends MetricCube<T, K, V, C, U>, U>
        extends Cube<C>, KeyReferable, MergedUnit<C> {

    DimensionSet dimensions();

    TimeBaselineDimension baselineDimension();

    V put(K key, V value);

    T table();

    Value query();

    Value query(long end);

    Value query(long start, long end);

    /**
     * @param from          开始时间戳
     * @param fromInclusive 是否包含开始
     * @param to            结束时间戳
     * @param toInclusive   是否包含开始
     * @return
     */
    default Value query(long from, boolean fromInclusive,
                        long to, boolean toInclusive) {
        throw new RuntimeException("需要重写query方法");
    }

    void expire(long expire);

    long expire();

    int eliminateExpiredData();
}