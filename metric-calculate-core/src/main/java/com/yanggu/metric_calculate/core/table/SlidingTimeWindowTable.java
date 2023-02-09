package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
public class SlidingTimeWindowTable<V extends MergedUnit<V> & Value<?>>
        implements Table<Long, V, Long, V, SlidingTimeWindowTable<V>>, Serializable {

    private Map<Tuple, V> twoKeyTable = new HashMap<>();

    @Override
    public void putValue(Long windowStartTime, Long windowEndTime, V value) {
        Tuple key = new Tuple(windowStartTime, windowEndTime);
        V v = twoKeyTable.get(key);
        //如果存在就合并
        if (v != null) {
            value.merge(v);
        }
        twoKeyTable.put(key, value);
    }

    @Override
    public SlidingTimeWindowTable<V> merge(SlidingTimeWindowTable<V> that) {
        that.twoKeyTable.forEach((tempTuple, otherValue) -> {
            V value = twoKeyTable.get(tempTuple);
            if (value == null) {
                value = otherValue;
            } else {
                value.merge(otherValue);
            }
            twoKeyTable.put(tempTuple, value);
        });
        return this;
    }

    @Override
    public V query(Long windowStartTime, boolean fromInclusive, Long windowEndTime, boolean toInclusive) {
        return twoKeyTable.get(new Tuple(windowStartTime, windowEndTime));
    }

    @Override
    public SlidingTimeWindowTable<V> cloneEmpty() {
        return new SlidingTimeWindowTable<>();
    }

    @Override
    public SlidingTimeWindowTable<V> fastClone() {
        Map<Tuple, V> newMap = new HashMap<>();
        twoKeyTable.forEach((k, v) -> newMap.put(k, v.fastClone()));
        SlidingTimeWindowTable<V> windowTable = new SlidingTimeWindowTable<>();
        windowTable.twoKeyTable = newMap;
        return windowTable;
    }

    @Override
    public boolean isEmpty() {
        return twoKeyTable.isEmpty();
    }

    /**
     * 删除过期数据
     * <p>将windowStart小于minTimestamp的数据全部删除</p>
     *
     * @param minTimestamp
     * @return
     */
    public int eliminateExpiredData(long minTimestamp) {
        int count = 0;
        if (CollUtil.isEmpty(twoKeyTable)) {
            return count;
        }
        Iterator<Map.Entry<Tuple, V>> iterator = twoKeyTable.entrySet().iterator();
        while (iterator.hasNext()) {
            Long windowStart = iterator.next().getKey().get(0);
            if (windowStart < minTimestamp) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

}
