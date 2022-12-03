package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnlimitedMergedUnit;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Cloneable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUnit<K extends Cloneable<K> & Comparable<K>, V extends Cloneable<V> & Value>
        implements UnlimitedMergedUnit<MapUnit<K, V>>, Value<Map<K, V>>, Serializable, Iterable<Map.Entry<K, V>> {

    private static final long serialVersionUID = -1300607404480893613L;

    public int limit = 0;

    private Map<K, V> details = new HashMap<>();

    public MapUnit() {
    }

    public MapUnit(KeyValue<K, V> keyValue) {
        this(keyValue.key(), (V) keyValue.value());
    }

    public MapUnit(K key, V value) {
        this();
        put(key, value);
    }

    /**
     * Construct.
     */
    public MapUnit(K key, V value, int limit) {
        this();
        put(key, value);
        this.limit = limit;
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

    /**
     * put.
     */
    public void put(K key, V value) {
        this.details.put(key, value);
        if (this.limit > 0 && this.details.size() > this.limit) {
            this.details.remove(this.details.keySet().iterator().next());
        }
    }

    public Map<K, V> asMap() {
        return this.details;
    }

    @Override
    public MapUnit<K, V> merge(MapUnit<K, V> that) {
        return merge(that, false);
    }

    private MapUnit<K, V> merge(MapUnit<K, V> that, boolean useLimit) {
        if (that == null) {
            return this;
        }
        this.limit = Math.max(this.limit, that.limit);
        for (Map.Entry<K, V> entry : that) {
            V thisValue = this.details.get(entry.getKey());
            if (thisValue != null) {
                if (thisValue instanceof MergedUnit && entry.getValue().getClass().equals(thisValue.getClass())) {
                    MergedUnit unit = (MergedUnit) thisValue;
                    unit.merge((MergedUnit) entry.getValue());
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
    public MapUnit<K, V> unlimitedMerge(MapUnit<K, V> that) {
        return merge(that, true);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.details.entrySet().iterator();
    }

    @Override
    public Map<K, V> value() {
        return this.details;
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, map=%s}", this.limit, this.details.toString());
    }

    @Override
    public MapUnit<K, V> fastClone() {
        MapUnit<K, V> result = new MapUnit<>();
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
        MapUnit<K, V> thatUnit = (MapUnit) that;
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
