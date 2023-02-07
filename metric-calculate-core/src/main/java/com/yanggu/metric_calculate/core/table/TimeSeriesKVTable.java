package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.cube.TimeReferable;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

import static com.yanggu.metric_calculate.core.enums.TimeUnit.MILLS;

/**
 * 时间序列存储
 * key是聚合后的时间戳, value是MergedUnit
 *
 * @param <V>
 */
@Data
public class TimeSeriesKVTable<V extends MergedUnit<V> & Value<?>> extends TreeMap<Long, V>
        implements KVTable<Long, V, TimeSeriesKVTable<V>>, Serializable {

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    @Override
    public V putValue(Long key, V value) {
        key = getActual(key);
        return compute(key, (k, v) -> v == null ? value : v.merge(value));
    }

    @Override
    public TimeSeriesKVTable<V> merge(TimeSeriesKVTable<V> that) {
        for (Map.Entry<Long, V> timeRow : that.entrySet()) {
            MergedUnit thisRow = get(timeRow.getKey());
            if (thisRow == null) {
                put(timeRow.getKey(), timeRow.getValue());
            } else if (thisRow.getClass().equals(timeRow.getValue().getClass())) {
                thisRow.merge(timeRow.getValue());
            }
        }
        return this;
    }

    @Override
    public Value<?> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(from, fromInclusive, to, toInclusive);

        Collection<V> values = subMap.values();

        if (CollUtil.isEmpty(values)) {
            return null;
        }

        return values.stream()
                .reduce(MergedUnit::merge)
                .orElseThrow(() -> new RuntimeException("merge失败"));
    }

    public int eliminateExpiredData(long minTimestamp) {
        int dataCount = 0;
        Iterator<Map.Entry<Long, V>> iterator = this.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey() < minTimestamp) {
                iterator.remove();
                dataCount++;
            }
        }
        return dataCount;
    }

    @Override
    public V getValue(Long key) {
        return get(getActual(key));
    }

    @Override
    public boolean existValue(Long key) {
        return containsKey(getActual(key));
    }

    @Override
    public V removeValue(Long key) {
        return remove(getActual(key));
    }

    private Long getActual(Long timestamp) {
        //如果不是毫秒值, 格式化到时间单位
        if (!timeBaselineDimension.getUnit().equals(MILLS)) {
            timestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        }
        return timestamp;
    }

    @Override
    public TimeSeriesKVTable<V> cloneEmpty() {
        TimeSeriesKVTable<V> result = new TimeSeriesKVTable<>();
        result.setTimeBaselineDimension(this.timeBaselineDimension);
        return result;
    }

    @Override
    public TimeSeriesKVTable<V> fastClone() {
        TimeSeriesKVTable<V> result = cloneEmpty();
        forEach((k, v) -> result.put(k, v.fastClone()));
        return result;
    }

    public TimeSeriesKVTable<V> subTable(Long start, boolean fromInclusive, Long end, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(start, fromInclusive, end, toInclusive);
        TimeSeriesKVTable<V> cloneEmpty = cloneEmpty();
        subMap.forEach((k, v) -> cloneEmpty.put(k, v.fastClone()));
        return cloneEmpty;
    }

}
