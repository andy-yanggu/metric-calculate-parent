package com.yanggu.metric_calculate.core.unit.map;

import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 同一账户过去30天所有交易账号的累计交易金额
 * <p>map的key是交易账号, value是累计交易金额</p>
 * <p>更加一般的情况是累计交易金额topN的账号列表</p>
 *
 * @param <K>
 * @param <V>
 */
@MapType
@MergeType(value = "BASEMAP", useParam = false)
public class BaseMapUnit<K extends Cloneable2<K> & Value<Object>, V extends Cloneable2<V> & MergedUnit<V>>
        implements MapUnit<K, V, BaseMapUnit<K, V>>, Value<Map<Object, Object>> {

    private Map<K, V> details = new HashMap<>();

    public BaseMapUnit() {
    }

    public BaseMapUnit(K key, V value) {
        this();
        put(key, value);
    }

    /**
     * put.
     */
    @Override
    public BaseMapUnit<K, V> put(K key, V value) {
        V thisValue = this.details.get(key);
        if (thisValue != null) {
            thisValue.merge(value);
            this.details.put(key, thisValue);
        } else {
            this.details.put(key, value);
        }
        return this;
    }

    public Map<K, V> getMap() {
        return this.details;
    }

    public void setMap(Map<K, V> paramMap) {
        this.details = paramMap;
    }

    @Override
    public BaseMapUnit<K, V> merge(BaseMapUnit<K, V> that) {
        for (Map.Entry<K, V> entry : that.details.entrySet()) {
            K key = entry.getKey().fastClone();
            V value = entry.getValue().fastClone();
            put(key, value);
        }
        return this;
    }

    @Override
    public Map<Object, Object> value() {
        Map<Object, Object> returnMap = new HashMap<>();
        details.forEach((k, v) -> returnMap.put(k.fastClone().value(), ((Value<?>) v.fastClone()).value()));
        return returnMap;
    }

    @Override
    public String toString() {
        return String.format("{map=%s}", JSONUtil.toJsonStr(this.details));
    }

    @Override
    public BaseMapUnit<K, V> fastClone() {
        BaseMapUnit<K, V> result = new BaseMapUnit<>();
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
        BaseMapUnit<K, V> thatUnit = (BaseMapUnit) that;
        if (this.details == null) {
            if (thatUnit.details != null) {
                return false;
            }
        } else if (!this.details.equals(thatUnit.details)) {
            return false;
        }
        return true;
    }

}
