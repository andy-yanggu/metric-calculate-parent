package com.yanggu.metriccalculate.cube;

import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.TimeReferable;
import com.yanggu.metriccalculate.value.Value;

import java.util.Map;

public interface MetricCube<T extends Table, K, V, C extends MetricCube<T, K, V, C, U>, U>
        extends Cube<C>, KeyReferable, TimeReferable, MergedUnit<C> {

    Map<String, Object> dimensions();

    TimeBaselineDimension baselineDimension();

    V put(K key, V value);

    T table();

    T table(String key);

    MetricCube table(String key, T table);

    Value query();

    Value query(long end);

    Value query(long start, long end);

    /**
     * @param from 开始时间戳
     * @param fromInclusive 是否包含开始
     * @param to 结束时间戳
     * @param toInclusive 是否包含开始
     * @return
     */
    default Value query(long from, boolean fromInclusive,
                        long to,   boolean toInclusive) {
        throw new RuntimeException("需要重写query方法");
    }

    void expire(long expire);

    long expire();

    int eliminateExpiredData();
}