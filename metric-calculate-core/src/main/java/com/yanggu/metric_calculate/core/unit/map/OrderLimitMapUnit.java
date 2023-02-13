package com.yanggu.metric_calculate.core.unit.map;

import cn.hutool.core.comparator.CompareUtil;
import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 同一账户过去30天所有交易账号的累计交易金额
 * <p>map的key是交易账号, value是累计交易金额</p>
 * <p>更加一般的情况是累计交易金额topN的账号列表</p>
 *
 * @param <K>
 * @param <V>
 */
@MergeType(value = "ORDERLIMITMAPUNIT", useParam = true)
@MapType(useCompareField = true, retainObject = false)
public class OrderLimitMapUnit<K extends Cloneable2<K> & Comparable<K>, V extends Cloneable2<V> & Value<?> & MergedUnit<V>>
        implements MapUnit<K, V, OrderLimitMapUnit<K, V>>, Value<List<K>> {

    private int limit = 0;

    private Map<K, V> details = new HashMap<>();

    public OrderLimitMapUnit() {
    }

    public OrderLimitMapUnit(Map<String, Object> params) {

    }

    public OrderLimitMapUnit(K key, V value) {
        this();
        put(key, value);
    }

    /**
     * Construct.
     */
    public OrderLimitMapUnit(K key, V value, int limit) {
        this();
        put(key, value);
        this.limit = limit;
    }

    /**
     * put.
     */
    @Override
    public OrderLimitMapUnit<K, V> put(K key, V value) {
        this.details.put(key, value);
        if (this.limit > 0 && this.details.size() > this.limit) {
            this.details.remove(this.details.keySet().iterator().next());
        }
        return this;
    }

    public Map<K, V> getMap() {
        return this.details;
    }

    public V get(K key) {
        return this.details.get(key);
    }

    public void setMap(Map<K, V> paramMap) {
        this.details = paramMap;
    }

    @Override
    public OrderLimitMapUnit<K, V> merge(OrderLimitMapUnit<K, V> that) {
        return merge(that, false);
    }

    private OrderLimitMapUnit<K, V> merge(OrderLimitMapUnit<K, V> that, boolean useLimit) {
        if (that == null) {
            return this;
        }
        this.limit = Math.max(this.limit, that.limit);
        for (Map.Entry<K, V> entry : that.details.entrySet()) {
            V thisValue = this.details.get(entry.getKey());
            if (thisValue != null) {
                if (entry.getValue().getClass().equals(thisValue.getClass())) {
                    thisValue.merge(entry.getValue());
                }
                continue;
            }
            K key = entry.getKey().fastClone();
            V value = entry.getValue().fastClone();
            if (useLimit) {
                this.details.put(key, value);
                continue;
            }
            put(key, value);
        }
        return this;
    }

    @Override
    public List<K> value() {
        List<Map.Entry<K, V>> list = new ArrayList<>(details.entrySet());
        List<K> collect = list.stream()
                .sorted((obj1, obj2) -> {
                    Object value1 = obj1.getValue().value();
                    Object value2 = obj2.getValue().value();
                    return CompareUtil.compare(value1, value2, false);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (limit > 0) {
            return new ArrayList<>(collect.subList(0, limit));
        } else {
            return collect;
        }
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, map=%s}", this.limit, this.details.toString());
    }

    @Override
    public OrderLimitMapUnit<K, V> fastClone() {
        OrderLimitMapUnit<K, V> result = new OrderLimitMapUnit<>();
        result.limit = this.limit;
        for (Map.Entry<K, V> entry : getMap().entrySet()) {
            K key = entry.getKey().fastClone();
            V value = entry.getValue().fastClone();
            result.getMap().put(key, value);
        }
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrderLimitMapUnit<K, V> thatUnit = (OrderLimitMapUnit) that;
        if (this.details == null) {
            if (thatUnit.details != null) {
                return false;
            }
        } else if (!this.details.equals(thatUnit.details)) {
            return false;
        }
        if (this.limit != thatUnit.limit) {
            return false;
        }
        return true;
    }

}
