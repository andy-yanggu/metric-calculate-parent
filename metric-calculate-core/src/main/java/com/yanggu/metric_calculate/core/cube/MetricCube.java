package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

/**
 *
 * @param <T> 底层存储的table
 * @param <K> KEY的类型
 * @param <V> MergedUnit类型
 * @param <C> MetricCube实现类
 */
public interface MetricCube<T extends Table, K, V, C extends MetricCube<T, K, V, C>>
        extends Cube<C>, KeyReferable, MergedUnit<C>, TimeReferable {

    DimensionSet getDimensionSet();

    void setDimensionSet(DimensionSet dimensionSet);

    TimeBaselineDimension getTimeBaselineDimension();

    void setTimeBaselineDimension(TimeBaselineDimension timeBaselineDimension);

    T getTable();

    void setTable(T table);

    void put(K key, V value);

    /**
     * 这里都是指窗口的开始时间和结束时间
     *
     * @param from          开始时间戳
     * @param fromInclusive 是否包含开始
     * @param to            结束时间戳
     * @param toInclusive   是否包含开始
     * @return
     */
    Value<?> query(K from, boolean fromInclusive, K to, boolean toInclusive);

    /**
     * 删除过期数据
     *
     * @return 删除的过期数据的数量
     */
    int eliminateExpiredData();

}