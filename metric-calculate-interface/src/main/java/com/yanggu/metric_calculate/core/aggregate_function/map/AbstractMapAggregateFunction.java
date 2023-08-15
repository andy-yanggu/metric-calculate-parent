package com.yanggu.metric_calculate.core.aggregate_function.map;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 映射类型的基类
 *
 * @param <K>        map的key
 * @param <V>        map的value输入值
 * @param <ValueACC> map的value聚合值
 * @param <ValueOUT> map的value输出值
 * @param <OUT>      输出值
 */
@Data
public abstract class AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, OUT>
        implements AggregateFunction<AbstractMap.SimpleImmutableEntry<K, V>, Map<K, ValueACC>, OUT> {

    protected AggregateFunction<V, ValueACC, ValueOUT> valueAggregateFunction;

    @Override
    public Map<K, ValueACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<K, ValueACC> add(AbstractMap.SimpleImmutableEntry<K, V> input, Map<K, ValueACC> accumulator) {
        K key = input.getKey();

        ValueACC oldAcc = accumulator.getOrDefault(key, valueAggregateFunction.createAccumulator());
        ValueACC newAcc = valueAggregateFunction.add(input.getValue(), oldAcc);
        accumulator.put(key, newAcc);
        return accumulator;
    }

    @Override
    public OUT getResult(Map<K, ValueACC> accumulator) {
        Map<K, ValueOUT> map = new HashMap<>();
        accumulator.forEach((tempKey, tempValueAcc) -> map.put(tempKey, valueAggregateFunction.getResult(tempValueAcc)));
        return (OUT) map;
    }

    @Override
    public Map<K, ValueACC> merge(Map<K, ValueACC> thisAccumulator, Map<K, ValueACC> thatAccumulator) {
        thatAccumulator.forEach((tempKey, thatAcc) -> {
            ValueACC thisAcc = thisAccumulator.getOrDefault(tempKey, valueAggregateFunction.createAccumulator());
            thisAcc = valueAggregateFunction.merge(thisAcc, thatAcc);
            thisAccumulator.put(tempKey, thisAcc);
        });
        return thisAccumulator;
    }

}
